/**
 * Copyright (c) 2017-2018 The Elastos Developers
 * <p>
 * Distributed under the MIT software license, see the accompanying file
 * LICENSE or https://opensource.org/licenses/mit-license.php
 */
package org.elastos.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import net.sf.json.JSONObject;
import org.elastos.conf.BasicConfiguration;
import org.elastos.conf.NodeConfiguration;
import org.elastos.conf.RetCodeConfiguration;
import org.elastos.entity.HdTxEntity;
import org.elastos.entity.RawTxEntity;
import org.elastos.entity.ReturnMsgEntity;
import org.elastos.exception.ApiRequestDataException;
import org.elastos.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author clark
 * <p>
 * Apr 21, 2018 12:45:54 PM
 */
@Service
public class ElaService {

    @Autowired
    private NodeConfiguration nodeConfiguration;
    @Autowired
    private BasicConfiguration basicConfiguration;
    @Autowired
    private RetCodeConfiguration retCodeConfiguration;


    private static Logger logger = LoggerFactory.getLogger(ElaService.class);

    public String sendRawTx(RawTxEntity rawTxEntity){
        String rawTx = JSON.toJSONString(rawTxEntity);
        ReturnMsgEntity.ELAReturnMsg msg = JsonUtil.jsonStr2Entity(HttpKit.post(nodeConfiguration.sendRawTransaction(),rawTx),ReturnMsgEntity.ELAReturnMsg.class);
        long status = 0;
        if(msg.getError() == 0){
            status = retCodeConfiguration.SUCC();
        }else{
            status = retCodeConfiguration.PROCESS_ERROR();
        }
        return JSON.toJSONString(new ReturnMsgEntity().setResult(msg.getResult()).setStatus(status));
    }

    /**
     * genHdTx info
     * @param hdTxEntity info entity
     * @return
     * @throws Exception
     */
    public String genHdTx(HdTxEntity hdTxEntity) throws Exception {

        String[] inputAddrs = hdTxEntity.getInputs();
        List<List<Map>> utxoList = new ArrayList<>();
        for (int i = 0; i < inputAddrs.length; i++) {

            String utxoStr = getUtxoByAddr(inputAddrs[i]);

            List<Map> utxo = stripUtxo(utxoStr);

            if(utxo != null){
                utxoList.add(utxo);
            }

        }

        return JSON.toJSONString(new ReturnMsgEntity().setResult(genHdTx(hdTxEntity, utxoList)).setStatus(retCodeConfiguration.SUCC()));

    }

    /**
     * get the current height of blockchain
     * @return
     */
    public String getCurrentHeight(){

        return reqChainData(nodeConfiguration.getBlockHeight());
    }

    /**
     * get block txs by height
     * @return
     */
    public String getBlockTxsByHeight(String height){

        return reqChainData(nodeConfiguration.getBlockTxByHeight()+ height);
    }

    /**
     * get block by height
     * @param height
     * @return
     */
    public String getBlockByHeight(String height){

        return reqChainData(nodeConfiguration.getBlockByHeight()+ height);
    }

    /**
     * get block by hash
     * @param hash
     * @return
     */
    public String getBlockByHash(String hash){

        return reqChainData(nodeConfiguration.getBlockByhash()+ hash);
    }

    /**
     * get transaction by hash
     * @param hash
     * @return
     */
    public String getTransactionByHash(String hash){

        return reqChainData(nodeConfiguration.getTransaction()+ hash);
    }

    /**
     * get address balance
     * @param address
     * @return
     */
    public String getBalance(String address){

        String result = HttpKit.get(nodeConfiguration.getUtxoByAddr()+ address);

        Map<String,Object>  resultMap = (Map<String,Object>) JSON.parse(result);

        Object resObj = resultMap.get("Result");

        if (resObj == null || StrKit.isBlank(resObj+"") || (resObj +"").equalsIgnoreCase("null")){

            return JSON.toJSONString(new ReturnMsgEntity().setResult("0.0").setStatus(retCodeConfiguration.SUCC()));

        }

        Map m = ((List<Map>)resultMap.get("Result")).get(0);

        List<Map> lm = (List<Map>)m.get("Utxo");

        BigDecimal total = new BigDecimal("0.0");

        for(int i=0;i<lm.size();i++){
            Map md = lm.get(i);
            BigDecimal v = new BigDecimal((String)md.get("Value"));
            total = total.add(v);
        }

        return JSON.toJSONString(new ReturnMsgEntity().setResult(total.toString()).setStatus(retCodeConfiguration.SUCC()));
    }

    /**
     * get address utxos
     * @param address
     * @return
     */
    public String getUtxos(String address){

        return reqChainData(nodeConfiguration.getUtxoByAddr()+ address);
    }

    /**
     * using http request chain data.
     * @param requestUrl
     * @return
     */
    private String reqChainData(String requestUrl){

        String result = HttpKit.get(requestUrl);

        Map<String,Object>  resultMap = (Map<String,Object>) JSON.parse(result);

        return JSON.toJSONString(new ReturnMsgEntity().setResult(resultMap.get("Result")).setStatus(retCodeConfiguration.SUCC()));

    }
    /**
     * genHdTx info
     * @param hdTxEntity info entity
     * @param utxoList addrs utxo list
     * @return
     * @throws Exception
     */
    private Map<String, Object> genHdTx(HdTxEntity hdTxEntity, List<List<Map>> utxoList) throws Exception {

        String data = hdTxEntity.getMemo();
        HdTxEntity.Output[] outputs = hdTxEntity.getOutputs();
        double smAmt = 0;
        for (int i = 0; i < outputs.length; i++) {
            smAmt += outputs[i].getAmt()/(basicConfiguration.ONE_ELA() * 1.0);
        }
        Map<String, Object> paraListMap = new HashMap<>();
        List txList = new ArrayList<>();
        paraListMap.put("Transactions", txList);
        Map txListMap = new HashMap();
        txList.add(txListMap);
        int index = -1;
        double spendMoney = 0.0;
        boolean hasEnoughFee = false;
        List utxoInputsArray = new ArrayList<>();
        txListMap.put("UTXOInputs", utxoInputsArray);
        for (int j = 0; j < utxoList.size(); j++) {
            List<Map> utxolm = utxoList.get(j);
            String addr = hdTxEntity.getInputs()[j];
            for (int i = 0; i < utxolm.size(); i++) {
                index = i;
                spendMoney += Double.valueOf(utxolm.get(i).get("Value") + "");
                if (Math.round(spendMoney * basicConfiguration.ONE_ELA()) >= Math.round((smAmt + basicConfiguration.FEE()) * basicConfiguration.ONE_ELA())) {
                    hasEnoughFee = true;
                    break;
                }
            }
            for (int i = 0; i <= index; i++) {
                Map<String, Object> utxoInputsDetail = new HashMap<>();
                Map<String, Object> utxoM = utxolm.get(i);
                utxoInputsDetail.put("txid", utxoM.get("Txid"));
                utxoInputsDetail.put("index", utxoM.get("Index"));
                utxoInputsDetail.put("address", addr);
                utxoInputsArray.add(utxoInputsDetail);
            }
            if (hasEnoughFee) {
                break;
            }
        }

        if (!hasEnoughFee) {
            throw new ApiRequestDataException("Not Enough UTXO");
        }
        List utxoOutputsArray = new ArrayList<>();
        txListMap.put("Outputs", utxoOutputsArray);
        for (int i = 0; i < outputs.length; i++) {
            Map<String, Object> utxoOutputsDetail = new HashMap<>();
            utxoOutputsDetail.put("address", outputs[i].getAddr());
            utxoOutputsDetail.put("amount", outputs[i].getAmt());
            utxoOutputsArray.add(utxoOutputsDetail);
        }
        double leftMoney = (spendMoney - (basicConfiguration.FEE() + smAmt));
        if (Math.round(leftMoney * basicConfiguration.ONE_ELA()) > Math.round(basicConfiguration.FEE() * basicConfiguration.ONE_ELA())) {
            Map<String, Object> utxoOutputsDetail = new HashMap<>();
            utxoOutputsDetail.put("address", hdTxEntity.getInputs()[0]);
            utxoOutputsDetail.put("amount", Math.round(leftMoney * basicConfiguration.ONE_ELA()));
            utxoOutputsArray.add(utxoOutputsDetail);
        }

        txListMap.put("Fee",basicConfiguration.FEE() * basicConfiguration.ONE_ELA());
        return paraListMap;
    }

    /**
     * @param result
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private List<Map> stripUtxo(String result) {

        Map m = JsonUtil.jsonToMap(JSONObject.fromObject(result));
        List<Map> lm = null;
        try {
            lm = ((List<Map>) m.get("Result"));
        } catch (Exception ex) {
            logger.warn(" address has no utxo yet .");
            return null;
        }
        List<Map> l = null;
        if (lm != null) {
            l = (List<Map>) lm.get(0).get("Utxo");
        }
        return l;
    }


    private String getUtxoByAddr(String addr) {

        String result = HttpKit.get(nodeConfiguration.getUtxoByAddr() + addr);

        return result;
    }


}
