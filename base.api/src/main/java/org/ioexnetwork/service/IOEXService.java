/**
 * Copyright (c) 2017-2018 The Elastos Developers
 * <p>
 * Distributed under the MIT software license, see the accompanying file
 * LICENSE or https://opensource.org/licenses/mit-license.php
 */
package org.ioexnetwork.service;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

import com.alibaba.fastjson.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.ioexnetwork.api.SingleSignTransaction;
import org.ioexnetwork.conf.BasicConfiguration;
import org.ioexnetwork.conf.DidConfiguration;
import org.ioexnetwork.conf.NodeConfiguration;
import org.ioexnetwork.conf.RetCodeConfiguration;
import org.ioexnetwork.ioex.*;
import org.ioexnetwork.entity.*;
import org.ioexnetwork.exception.ApiRequestDataException;
import org.ioexnetwork.util.*;
import org.ioexnetwork.util.ioex.IOEXHdSupport;
import org.ioexnetwork.util.ioex.IOEXKit;
import org.ioexnetwork.util.ioex.IOEXSignTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;


@Service
public class IOEXService {

    private static final String CHARSET = "UTF-8";

    @Autowired
    private NodeConfiguration nodeConfiguration;
    @Autowired
    private BasicConfiguration basicConfiguration;
    @Autowired
    private RetCodeConfiguration retCodeConfiguration;
    @Autowired
    private DidConfiguration didConfiguration;

    private static Logger logger = LoggerFactory.getLogger(IOEXService.class);

    /*
     create a IOEX wallet
     */
    public String createWallet(){
        JSONObject result = new JSONObject();
        String privateKey = IOEX.getPrivateKey();
        String publicKey  = IOEX.getPublicFromPrivate(privateKey);
        String publicAddr = IOEX.getAddressFromPrivate(privateKey);
        result.put("privateKey",privateKey);
        result.put("publicKey",publicKey);
        result.put("address",publicAddr);
        return JSON.toJSONString(new ReturnMsgEntity().setResult(result).setStatus(retCodeConfiguration.SUCC()));
    }

    public String mnemonic(String type){
        return JSON.toJSONString(new ReturnMsgEntity().setResult(IOEXHdSupport.generateMnemonic(type.equals("chinese")?MnemonicType.CHINESE:MnemonicType.ENGLISH)).setStatus(retCodeConfiguration.SUCC()));
    }

    public String genHdWallet(HdWalletEntity entity) throws Exception{
        JSONArray array = new JSONArray();
        String mnemonic = entity.getMnemonic();
        Integer start = entity.getStart();
        Integer end = entity.getEnd();
        Integer index = entity.getIndex();
        if(mnemonic != null && index != null){
            return genHdWallet(mnemonic,index);
        }
        if(mnemonic == null || start < 0 || start > end){
            throw new ApiRequestDataException("invalid param");
        }
        for(int i=start;i<=end;i++){
            array.add(JSONObject.fromObject(IOEXHdSupport.generate(mnemonic,i)));
        }
        return JSON.toJSONString(new ReturnMsgEntity().setResult(array).setStatus(retCodeConfiguration.SUCC()));
    }

    public String genHdWallet(String mnemonic,int index) throws Exception{

        return JSON.toJSONString(new ReturnMsgEntity().setResult(JSONObject.fromObject(IOEXHdSupport.generate(mnemonic,index))).setStatus(retCodeConfiguration.SUCC()));
    }

    /*
     get transaction by transaction id
     */
    public String getTxByTxId(String txid){
        return reqChainData(nodeConfiguration.sendRawTransaction(ChainType.MAIN_CHAIN)+ "/" + txid);
    }

    public String sendRawTx(RawTxEntity rawTxEntity){
        String rawTx = JSON.toJSONString(rawTxEntity);
        ChainType type = rawTxEntity.getType();
        ReturnMsgEntity.IOEXReturnMsg msg = JsonUtil.jsonStr2Entity(HttpKit.post(nodeConfiguration.sendRawTransaction(type),rawTx),ReturnMsgEntity.IOEXReturnMsg.class);
        long status = 0;
        if(msg.getError() == 0){
            status = retCodeConfiguration.SUCC();
        }else{
            status = retCodeConfiguration.PROCESS_ERROR();
        }
        return JSON.toJSONString(new ReturnMsgEntity().setResult(msg.getResult()).setStatus(status));
    }

    public String getTxsByTxIds(List<String> strList){
        List<Object> results = new ArrayList<>();
        for(int i=0;i<strList.size();i++){
            String txid = strList.get(i);
            String result = HttpKit.get(nodeConfiguration.sendRawTransaction(ChainType.MAIN_CHAIN)+ "/" + txid);
            Map<String,Object>  resultMap = (Map<String,Object>) JSON.parse(result);
            results.add(resultMap.get("Result"));
        }
        return JSON.toJSONString(new ReturnMsgEntity().setResult(results).setStatus(retCodeConfiguration.SUCC()));
    }

    public String genHdTx(HdTxEntity hdTxEntity) throws Exception {

        List<List<Map>> utxoList = remakeHdEntity(hdTxEntity);

        return JSON.toJSONString(new ReturnMsgEntity().setResult(genHdTx(hdTxEntity, utxoList)).setStatus(retCodeConfiguration.SUCC()));

    }

    private static final Long IOEX_TO_SIOEX = 100000000l;
    
    public String genVoteHdTx(HdTxEntity hdTxEntity) throws Exception {

        String[] inputs = hdTxEntity.getInputs();
        long total = 0;
        for(int i=0;i< inputs.length;i++){
            String input = inputs[i];
            Map<String,Object> balance = (Map)JSON.parse(getBalance(input));
            total += Double.valueOf(balance.get("result") +"") * IOEX_TO_SIOEX;
        }
        long spend = 0;
        HdTxEntity.Output[] outputs = hdTxEntity.getOutputs();
        for(int i=0;i< outputs.length;i++){
            spend += outputs[i].getAmt();
        }
        long left = total - spend - new BigDecimal(basicConfiguration.FEE() * IOEX_TO_SIOEX).longValue();
        HdTxEntity.Output leftoutput = new HdTxEntity.Output();
        leftoutput.setAddr(inputs[0]);
        leftoutput.setAmt(left);
        HdTxEntity.Output[] desc = new HdTxEntity.Output[outputs.length+1];
        System.arraycopy(outputs,0,desc,0,outputs.length);
        desc[desc.length -1] = leftoutput;
        hdTxEntity.setOutputs(desc);
        List<List<Map>> utxoList = remakeHdEntity(hdTxEntity);

        return JSON.toJSONString(new ReturnMsgEntity().setResult(genHdTx(hdTxEntity, utxoList)).setStatus(retCodeConfiguration.SUCC()));

    }

    private List<List<Map>> remakeHdEntity(HdTxEntity hdTxEntity){

        String[] inputAddrs = hdTxEntity.getInputs();

        List<List<Map>> utxoList = new ArrayList<>();

        List<String> inputs = new ArrayList<>();

        for (int i = 0; i < inputAddrs.length; i++) {

            List<String> utxoStr = getUtxoByAddr(inputAddrs[i],ChainType.MAIN_CHAIN);

            List<Map> utxo = stripUtxo(utxoStr.get(0));

            if(utxo != null){
                inputs.add(inputAddrs[i]);
                utxoList.add(utxo);
            }

        }
        hdTxEntity.setInputs(inputs.toArray(new String[inputs.size()]));
        return utxoList;
    }

    public String genCrossHdTx(HdTxEntity hdTxEntity) throws Exception {

        List<List<Map>> utxoList = remakeHdEntity(hdTxEntity);

        return JSON.toJSONString(new ReturnMsgEntity().setResult(genCrossHdTx(hdTxEntity, utxoList)).setStatus(retCodeConfiguration.SUCC()));

    }

    /*
     get the current height of blockchain
     */
    public String getCurrentHeight(){

        return reqChainData(nodeConfiguration.getBlockHeight(ChainType.MAIN_CHAIN));
    }

    /*
     get block txs by height
     */
    public String getBlockTxsByHeight(String height){

        return reqChainData(nodeConfiguration.getBlockTxByHeight(ChainType.MAIN_CHAIN)+ height);
    }

    /*
     get block by height
     */
    public String getBlockByHeight(String height){

        return reqChainData(nodeConfiguration.getBlockByHeight(ChainType.MAIN_CHAIN)+ height);
    }

    /*
     get block by hash
     */
    public String getBlockByHash(String hash){

        return reqChainData(nodeConfiguration.getBlockByhash(ChainType.MAIN_CHAIN)+ hash);
    }

    /*
     get transaction by hash
     */
    public String getTransactionByHash(String hash){

        return reqChainData(nodeConfiguration.getTransaction(ChainType.MAIN_CHAIN)+ hash);
    }

    /*
     get transaction by hash
     */
    public String getTransactionByHash(String hash,ChainType type){

        return reqChainData(nodeConfiguration.getTransaction(type)+ hash);
    }

    /*
     get address balance
     */
    public String getBalance(String address){

        checkAddr(address);

        String result = HttpKit.get(nodeConfiguration.getUtxoByAddr(ChainType.MAIN_CHAIN)+ address);

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

    public String getUtxos(String address){

        checkAddr(address);

        return reqChainData(nodeConfiguration.getUtxoByAddr(ChainType.MAIN_CHAIN)+ address);
    }

    private void checkAddr(String address){
        if (!IOEXKit.checkAddress(address)){
            throw new ApiRequestDataException(Errors.IOEX_ADDRESS_INVALID.val() + ":" + address);
        }
    }

    public String createDid() throws Exception{
        JSONObject result = new JSONObject();
        String privKey = IOEX.getPrivateKey();
        String did = IOEX.getIdentityIDFromPrivate(privKey);
        result.put("privateKey",privKey);
        result.put("did",did);
        return JSON.toJSONString(new ReturnMsgEntity().setResult(result).setStatus(retCodeConfiguration.SUCC()));
    }

    public String sign(SignDataEntity entity)  throws Exception{
        JSONObject result = new JSONObject();
        String msg = entity.getMsg();
        String privateKey = entity.getPrivateKey();
        ECKey ec = ECKey.fromPrivate(DatatypeConverter.parseHexBinary(privateKey));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.write(msg.getBytes(CHARSET));
        byte[] signature = SignTool.doSign(baos.toByteArray(), DatatypeConverter.parseHexBinary(privateKey));
        byte[] code = new byte[33];
        System.arraycopy(Util.CreateSingleSignatureRedeemScript(ec.getPubBytes(),1), 1,code,0,code.length);
        result.put("msg",DatatypeConverter.printHexBinary(msg.getBytes(CHARSET)));
        result.put("pub",DatatypeConverter.printHexBinary(code));
        result.put("sig",DatatypeConverter.printHexBinary(signature));
        return JSON.toJSONString(new ReturnMsgEntity().setResult(result).setStatus(retCodeConfiguration.SUCC()));
    }

    public String verify(SignDataEntity entity){
        String hexMsg = entity.getMsg();
        String hexSig = entity.getSig();
        String hexPub = entity.getPub();
        byte[] msg = DatatypeConverter.parseHexBinary(hexMsg);
        byte[] sig = DatatypeConverter.parseHexBinary(hexSig);
        byte[] pub = DatatypeConverter.parseHexBinary(hexPub);
        boolean isVerify = IOEXSignTool.verify(msg,sig,pub);
        return JSON.toJSONString(new ReturnMsgEntity().setResult(isVerify).setStatus(retCodeConfiguration.SUCC()));
    }

    public String retriveDid(String privateKey) throws Exception {

        String did = IOEX.getIdentityIDFromPrivate(privateKey);

        return JSON.toJSONString(new ReturnMsgEntity().setResult(did).setStatus(retCodeConfiguration.SUCC()));

    }

    private String reqChainData(String requestUrl){

        String result = HttpKit.get(requestUrl);

        Map<String,Object>  resultMap = (Map<String,Object>) JSON.parse(result);

        return JSON.toJSONString(new ReturnMsgEntity().setResult(resultMap.get("Result")).setStatus(retCodeConfiguration.SUCC()));

    }
    
    private Map<String, Object> genHdTx(HdTxEntity hdTxEntity, List<List<Map>> utxoList) throws Exception {

        String data = hdTxEntity.getMemo();
        HdTxEntity.Output[] outputs = hdTxEntity.getOutputs();
        double smAmt = 0;
        for (int i = 0; i < outputs.length; i++) {
            smAmt += outputs[i].getAmt()/(basicConfiguration.ONE_IOEX() * 1.0);
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
                if (Math.round(spendMoney * basicConfiguration.ONE_IOEX()) >= Math.round((smAmt + basicConfiguration.FEE()) * basicConfiguration.ONE_IOEX())) {
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
        if (Math.round(leftMoney * basicConfiguration.ONE_IOEX()) > 0){
            Map<String, Object> utxoOutputsDetail = new HashMap<>();
            utxoOutputsDetail.put("address", hdTxEntity.getInputs()[0]);
            utxoOutputsDetail.put("amount", Math.round(leftMoney * basicConfiguration.ONE_IOEX()));
            utxoOutputsArray.add(utxoOutputsDetail);
        }

        txListMap.put("Fee",basicConfiguration.FEE() * basicConfiguration.ONE_IOEX());
        return paraListMap;
    }

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


    private List<String> getUtxoByAddr(List<String> addrs,ChainType type) {
        List<String> rstlist = new ArrayList<>();
        for(int i=0;i<addrs.size();i++){
            String addr = addrs.get(i);
            checkAddr(addr);
            String result = HttpKit.get(nodeConfiguration.getUtxoByAddr(type) + addr);
            rstlist.add(result);
        }
        return rstlist;
    }

    private List<String> getUtxoByAddr(String addr,ChainType type) {
        List<String> addrLst = new ArrayList<>();
        addrLst.add(addr);
        return getUtxoByAddr(addrLst,type);
    }

    @SuppressWarnings("unchecked")
    public String transfer(TransferParamEntity param) throws Exception {

        List<LinkedHashMap> rcv = (List<LinkedHashMap>) param.getReceiver();
        List<Map> sdr = (List<Map>) param.getSender();
        List<String> addrList = new ArrayList<>();
        List<Double> valList = new ArrayList<>();
        Double totalAmt = 0.0;
        for(int i=0;i<rcv.size();i++){
            Map m = rcv.get(i);
            addrList.add((String)m.get("address"));
            Double tmpAmt = Double.valueOf((String)m.get("amount"));
            valList.add(tmpAmt);
            totalAmt += tmpAmt;
        }
        List<String> sdrAddrs = new ArrayList<>();
        List<String> sdrPrivs = new ArrayList<>();
        for(int i=0;i<sdr.size();i++){
            Map m = sdr.get(i);
            String address = (String) m.get("address");
            String privKey = (String) m.get("privateKey");
            sdrAddrs.add(address);
            sdrPrivs.add(privKey);
        }
        String memo = param.getMemo();
        ChainType type = param.getType();
        String response = gen(totalAmt, sdrPrivs , sdrAddrs,
                addrList, valList, memo,type,null);
        Object orst =((Map<String, Object>) JSON.parse(response)).get("Result");
        if ((orst instanceof Map) == false){
            throw new ApiRequestDataException("Not valid request Data");
        }
        Map<String,Object> rawM = (Map<String, Object>)orst;
        String rawTx = (String) rawM.get("rawTx");
        String txHash = (String) rawM.get("txHash");
        logger.info("rawTx:" + rawTx + ", txHash :" + txHash);

        return sendTx(rawTx,type);
    }

    public boolean voteValidate(List<String> sdr , List<String> rcv){

        for(int i=0;i<rcv.size();i++){
            if(sdr == null || !sdr.contains(rcv.get(i))){
                return false;
            }
        }

        return true;
    }

    @SuppressWarnings("unchecked")
    public String dposVote(TransferParamEntity param) throws Exception {

        List<LinkedHashMap> rcv = (List<LinkedHashMap>) param.getReceiver();
        List<Map> sdr = (List<Map>) param.getSender();
        List<String> addrList = new ArrayList<>();
        List<Double> valList = new ArrayList<>();
        Double totalAmt = 0.0;
        List<List<String>> candidatePublicKeys = new ArrayList<>();
        for(int i=0;i<rcv.size();i++){
            Map m = rcv.get(i);
            addrList.add((String)m.get("address"));
            Double tmpAmt = Double.valueOf((String)m.get("amount"));
            valList.add(tmpAmt);
            totalAmt += tmpAmt;
            candidatePublicKeys.add((ArrayList<String>)m.get("candidatePublicKeys"));
        }
        List<String> sdrAddrs = new ArrayList<>();
        List<String> sdrPrivs = new ArrayList<>();
        for(int i=0;i<sdr.size();i++){
            Map m = sdr.get(i);
            String address = (String) m.get("address");
            String privKey = (String) m.get("privateKey");
            sdrAddrs.add(address);
            sdrPrivs.add(privKey);
        }
        if(!voteValidate(sdrAddrs,addrList)){
            throw new RuntimeException("output address must at least be one of input address");
        }
        String memo = param.getMemo();
        ChainType type = param.getType();
        String response = gen(totalAmt, sdrPrivs , sdrAddrs,
                addrList, valList, memo,type,candidatePublicKeys);
        Object orst =((Map<String, Object>) JSON.parse(response)).get("Result");
        if ((orst instanceof Map) == false){
            throw new ApiRequestDataException("Not valid request Data");
        }
        Map<String,Object> rawM = (Map<String, Object>)orst;
        String rawTx = (String) rawM.get("rawTx");
        String txHash = (String) rawM.get("txHash");
        logger.info("rawTx:" + rawTx + ", txHash :" + txHash);

        return sendTx(rawTx,type);
    }


    /*
     send a transaction to blockchain.
     */
    @SuppressWarnings("rawtypes")
    public String gen(double smAmt , List<String> prvKeys , List<String> sdrAddrs ,List<String> addrs , List<Double> amts , String data,ChainType type,List<List<String>> candidatePublicKeys) throws Exception {

        List<String> utxoStrLst = getUtxoByAddr(sdrAddrs,type);
        List<List<Map>> utxoTotal = new ArrayList<>();
        for(int i=0;i<utxoStrLst.size();i++){
            String utxoStr = utxoStrLst.get(i);
            List<Map> utxo = stripUtxo(utxoStr);
            utxoTotal.add(utxo);
        }

        if(utxoTotal == null){
            throw new ApiRequestDataException(Errors.NOT_ENOUGH_UTXO.val());
        }

        if(type == ChainType.MAIN_DID_CROSS_CHAIN || type == ChainType.DID_MAIN_CROSS_CHAIN) {
            return genCrossTx(smAmt,utxoTotal,prvKeys, sdrAddrs, addrs, amts, data,type);
        }

        return genTx(smAmt, utxoTotal, prvKeys, sdrAddrs, addrs, amts, data,candidatePublicKeys);
    }

    public String genCrossTx(double smAmt , List<List<Map>> utxoTotal , List<String> prvKeys , List<String> sdrAddrs ,List<String> addrs ,
                             List<Double> amts , String data,ChainType type) throws Exception {

        if(addrs == null || addrs.size() == 0) {
            throw new RuntimeException("output can not be blank");
        }

        Map<String,Object> paraListMap = new HashMap<>();
        List txList = new ArrayList<>();
        paraListMap.put("Transactions", txList);
        Map<String,Object> txListMap = new HashMap<>();
        txList.add(txListMap);

        int index = -1;
        double spendMoney = 0.0;
        boolean hasEnoughFee = false;
        int utxoIndex = -1;
        out :for(int z= 0 ;z < utxoTotal.size();z++){
            List<Map> utxolm = utxoTotal.get(z);
            utxoIndex = z;
            for( int i=0; i<utxolm.size(); i++) {
                index = i;
                spendMoney += Double.valueOf(utxolm.get(i).get("Value")+"");
                if( Math.round(spendMoney * basicConfiguration.ONE_IOEX()) >= Math.round((smAmt + (basicConfiguration.CROSS_CHAIN_FEE() * 2)) * basicConfiguration.ONE_IOEX())) {
                    hasEnoughFee = true;
                    break out;
                }
            }
        }


        if(!hasEnoughFee) {
            throw new ApiRequestDataException(Errors.NOT_ENOUGH_UTXO.val());
        }

        List utxoInputsArray = new ArrayList<>();
        txListMap.put("UTXOInputs", utxoInputsArray);
        List privsArray = new ArrayList<>();
        for(int z=0;z<=utxoIndex;z++){
            List<Map> utxolm = utxoTotal.get(z);
            String privateKey = prvKeys.get(z);
            String addr = sdrAddrs.get(z);
            int subIndex = utxolm.size() - 1;
            if(z == utxoIndex){
                subIndex = index;
            }
            for(int i=0;i<=subIndex;i++) {
                Map<String,Object> utxoInputsDetail = new HashMap<>();
                Map<String,Object> utxoM = utxolm.get(i);
                Map<String,Object> privM = new HashMap<>();
                utxoInputsDetail.put("txid",  utxoM.get("Txid"));
                utxoInputsDetail.put("index",  utxoM.get("Index"));
                utxoInputsDetail.put("address",  addr);
                privM.put("privateKey",  privateKey);
                utxoInputsArray.add(utxoInputsDetail);
                privsArray.add(privM);
            }
        }
        List utxoOutputsArray = new ArrayList<>();
        txListMap.put("Outputs", utxoOutputsArray);
        Map<String,Object> brokerOutputs = new HashMap<>();
        if (type == ChainType.MAIN_DID_CROSS_CHAIN){
            brokerOutputs.put("address", didConfiguration.getMainChainAddress());
        }else if(type == ChainType.DID_MAIN_CROSS_CHAIN){
            brokerOutputs.put("address", didConfiguration.getBurnAddress());
        }else{
            throw new ApiRequestDataException("no such transfer type");
        }
        brokerOutputs.put("amount", Math.round((smAmt+basicConfiguration.CROSS_CHAIN_FEE()) * basicConfiguration.ONE_IOEX()));
        utxoOutputsArray.add(brokerOutputs);

        double leftMoney = (spendMoney - ((basicConfiguration.CROSS_CHAIN_FEE() * 2) + smAmt));
        String changeAddr = sdrAddrs.get(0);
        Map<String,Object> utxoOutputsDetail = new HashMap<>();
        utxoOutputsDetail.put("address", changeAddr);
        utxoOutputsDetail.put("amount",Math.round(leftMoney * basicConfiguration.ONE_IOEX()));
        utxoOutputsArray.add(utxoOutputsDetail);

        txListMap.put("PrivateKeySign",privsArray);
        List crossOutputsArray = new ArrayList<>();
        txListMap.put("CrossChainAsset",crossOutputsArray);
        for(int i=0;i<addrs.size();i++) {
            utxoOutputsDetail = new HashMap<>();
            utxoOutputsDetail.put("address", addrs.get(i));
            utxoOutputsDetail.put("amount", Math.round(amts.get(i) * basicConfiguration.ONE_IOEX()));
            crossOutputsArray.add(utxoOutputsDetail);
        }

        JSONObject par = new JSONObject();
        par.accumulateAll(paraListMap);
        logger.info("sending : " + par);
        String rawTx = null ;
        rawTx = SingleSignTransaction.genCrossChainRawTransaction(par);
        logger.info("receiving : " + rawTx);
        return rawTx;
    }

    public Map<String, Object> genCrossHdTx(HdTxEntity hdTxEntity, List<List<Map>> utxoTotal) throws Exception {

        HdTxEntity.Output[] outputs =  hdTxEntity.getOutputs();
        String[] sdrAddrsArr = hdTxEntity.getInputs();
        if (outputs.length == 0 || sdrAddrsArr.length == 0){
            throw new ApiRequestDataException("outputs or inputs can not be blank");
        }
        double smAmt = 0.0;
        List<String> sdrAddrs = Arrays.asList(sdrAddrsArr);
        List<String> addrs = new ArrayList<>();
        List<Double> amts = new ArrayList<>();
        ChainType type = ChainType.MAIN_DID_CROSS_CHAIN;
        for(int i=0;i<outputs.length;i++){
            HdTxEntity.Output output = outputs[i];
            double amt = output.getAmt() * 1.0/basicConfiguration.ONE_IOEX();
            smAmt += amt;
            addrs.add(output.getAddr());
            amts.add(amt);
        }
        Map<String,Object> paraListMap = new HashMap<>();
        List txList = new ArrayList<>();
        paraListMap.put("Transactions", txList);
        Map<String,Object> txListMap = new HashMap<>();
        txList.add(txListMap);

        int index = -1;
        double spendMoney = 0.0;
        boolean hasEnoughFee = false;
        int utxoIndex = -1;
        out :for(int z= 0 ;z < utxoTotal.size();z++){
            List<Map> utxolm = utxoTotal.get(z);
            utxoIndex = z;
            for( int i=0; i<utxolm.size(); i++) {
                index = i;
                spendMoney += Double.valueOf(utxolm.get(i).get("Value")+"");
                if( Math.round(spendMoney * basicConfiguration.ONE_IOEX()) >= Math.round((smAmt + (basicConfiguration.CROSS_CHAIN_FEE() * 2)) * basicConfiguration.ONE_IOEX())) {
                    hasEnoughFee = true;
                    break out;
                }
            }
        }


        if(!hasEnoughFee) {
            throw new ApiRequestDataException(Errors.NOT_ENOUGH_UTXO.val());
        }

        List utxoInputsArray = new ArrayList<>();
        txListMap.put("UTXOInputs", utxoInputsArray);
        for(int z=0;z<=utxoIndex;z++){
            List<Map> utxolm = utxoTotal.get(z);
            String addr = sdrAddrs.get(z);
            int subIndex = utxolm.size() - 1;
            if(z == utxoIndex){
                subIndex = index;
            }
            for(int i=0;i<=subIndex;i++) {
                Map<String,Object> utxoInputsDetail = new HashMap<>();
                Map<String,Object> utxoM = utxolm.get(i);
                utxoInputsDetail.put("txid",  utxoM.get("Txid"));
                utxoInputsDetail.put("index",  utxoM.get("Index"));
                utxoInputsDetail.put("address",  addr);
                utxoInputsArray.add(utxoInputsDetail);
            }
        }
        List utxoOutputsArray = new ArrayList<>();
        txListMap.put("Outputs", utxoOutputsArray);
        Map<String,Object> brokerOutputs = new HashMap<>();
        if (type == ChainType.MAIN_DID_CROSS_CHAIN){
            brokerOutputs.put("address", didConfiguration.getMainChainAddress());
        }else if(type == ChainType.DID_MAIN_CROSS_CHAIN){
            brokerOutputs.put("address", didConfiguration.getBurnAddress());
        }else{
            throw new ApiRequestDataException("no such transfer type");
        }
        brokerOutputs.put("amount", Math.round((smAmt+basicConfiguration.CROSS_CHAIN_FEE()) * basicConfiguration.ONE_IOEX()));
        utxoOutputsArray.add(brokerOutputs);

        double leftMoney = (spendMoney - ((basicConfiguration.CROSS_CHAIN_FEE() * 2) + smAmt));
        String changeAddr = sdrAddrs.get(0);
        Map<String,Object> utxoOutputsDetail = new HashMap<>();
        utxoOutputsDetail.put("address", changeAddr);
        utxoOutputsDetail.put("amount",Math.round(leftMoney * basicConfiguration.ONE_IOEX()));
        utxoOutputsArray.add(utxoOutputsDetail);

        List crossOutputsArray = new ArrayList<>();
        txListMap.put("CrossChainAsset",crossOutputsArray);

        for(int i=0;i<addrs.size();i++) {
            utxoOutputsDetail = new HashMap<>();
            utxoOutputsDetail.put("address", addrs.get(i));
            utxoOutputsDetail.put("amount", Math.round(amts.get(i) * basicConfiguration.ONE_IOEX()));
            crossOutputsArray.add(utxoOutputsDetail);
        }
        txListMap.put("Fee",basicConfiguration.CROSS_CHAIN_FEE() * basicConfiguration.ONE_IOEX() * 2);

        return paraListMap;
    }

    /**
     * generate raw transaction.
     * @param smAmt the total spend money
     * @param utxoTotal utxo list
     * @param prvKeys private key list
     * @param sdrAddrs sender address
     * @param addrs receiver addresses
     * @param amts receiver output money
     * @param data memo data
     * @param candidatePublicKeys candidate Public Keys
     * @return String
     * @throws Exception Exception
     */
    @SuppressWarnings({ "rawtypes", "unchecked", "static-access" })
    public String genTx(double smAmt , List<List<Map>> utxoTotal , List<String> prvKeys , List<String> sdrAddrs ,List<String> addrs , List<Double> amts , String data,List<List<String>> candidatePublicKeys) throws Exception {

        if(addrs == null || addrs.size() == 0) {
            throw new RuntimeException("output can not be blank");
        }

        Map<String,Object> paraListMap = new HashMap<>();
        List txList = new ArrayList<>();
        paraListMap.put("Transactions", txList);
        Map<String,Object> txListMap = new HashMap<>();
        txList.add(txListMap);
        if(!StrKit.isBlank(data)) {
            txListMap.put("Memo", data);
        }

        int index = -1;
        double spendMoney = 0.0;
        boolean hasEnoughFee = false;
        int utxoIndex = -1;
        out :for(int z= 0 ;z < utxoTotal.size();z++){
            List<Map> utxolm = utxoTotal.get(z);
            utxoIndex = z;
            for( int i=0; i<utxolm.size(); i++) {
                index = i;
                spendMoney += Double.valueOf(utxolm.get(i).get("Value")+"");
                if( Math.round(spendMoney * basicConfiguration.ONE_IOEX()) >= Math.round((smAmt + basicConfiguration.FEE()) * basicConfiguration.ONE_IOEX())) {
                    hasEnoughFee = true;
                    break out;
                }
            }
        }


        if(!hasEnoughFee) {
            throw new ApiRequestDataException(Errors.NOT_ENOUGH_UTXO.val());
        }

        List utxoInputsArray = new ArrayList<>();
        txListMap.put("UTXOInputs", utxoInputsArray);
        for(int z=0;z<=utxoIndex;z++){
            List<Map> utxolm = utxoTotal.get(z);
            String privateKey = prvKeys.get(z);
            String addr = sdrAddrs.get(z);
            int subIndex = utxolm.size() - 1;
            if(z == utxoIndex){
                subIndex = index;
            }
            for(int i=0;i<=subIndex;i++) {
                Map<String,Object> utxoInputsDetail = new HashMap<>();
                Map<String,Object> utxoM = utxolm.get(i);
                utxoInputsDetail.put("txid",  utxoM.get("Txid"));
                utxoInputsDetail.put("index",  utxoM.get("Index"));
                utxoInputsDetail.put("privateKey",  privateKey);
                utxoInputsDetail.put("address",  addr);
                utxoInputsArray.add(utxoInputsDetail);
            }
        }
        List utxoOutputsArray = new ArrayList<>();
        txListMap.put("Outputs", utxoOutputsArray);
        for(int i=0;i<addrs.size();i++) {
            Map<String,Object> utxoOutputsDetail = new HashMap<>();
            utxoOutputsDetail.put("address", addrs.get(i));
            utxoOutputsDetail.put("amount", Math.round(amts.get(i) * basicConfiguration.ONE_IOEX()));
            if(candidatePublicKeys != null && candidatePublicKeys.size() > 0){
                Map<String,Object> payload = new HashMap<>();
                payload.put("type","vote");
                payload.put("candidatePublicKeys",candidatePublicKeys.get(i));
                utxoOutputsDetail.put("payload",payload);
            }
            utxoOutputsArray.add(utxoOutputsDetail);
        }
        if(candidatePublicKeys != null && !voteValidate(sdrAddrs.subList(0,utxoIndex+1),addrs)){
            throw new RuntimeException("actual spend input address can not find output address , try spend more coin ");
        }
        double leftMoney = (spendMoney - (basicConfiguration.FEE() + smAmt));
        if (Math.round(leftMoney * basicConfiguration.ONE_IOEX()) > 0){
            String changeAddr = sdrAddrs.get(0);
            Map<String,Object> utxoOutputsDetail = new HashMap<>();
            utxoOutputsDetail.put("address", changeAddr);
            utxoOutputsDetail.put("amount",Math.round(leftMoney * basicConfiguration.ONE_IOEX()));
            utxoOutputsArray.add(utxoOutputsDetail);
        }
        JSONObject par = new JSONObject();
        par.accumulateAll(paraListMap);
        logger.info("sending : " + par);
        String rawTx = null ;
        rawTx = IOEXKit.genRawTransaction(par);
        logger.info("receiving : " + rawTx);
        return rawTx;
    }

    @SuppressWarnings("static-access")
    public String sendTx(String rawData,ChainType type) {
        RawTxEntity entity = new RawTxEntity();
        entity.setData(rawData);
        entity.setType(type);
        return sendRawTx(entity);
    }

    /*
     make mainchain to did chain asset transfer
     */
    public String main2DidCrossTransfer(TransferParamEntity entity) throws Exception{
        entity.setType(ChainType.MAIN_DID_CROSS_CHAIN);
        return transfer(entity);
    }

}
