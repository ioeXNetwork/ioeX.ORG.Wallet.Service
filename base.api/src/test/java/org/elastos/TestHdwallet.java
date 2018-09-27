/**
 * Copyright (c) 2017-2018 The Elastos Developers
 * <p>
 * Distributed under the MIT software license, see the accompanying file
 * LICENSE or https://opensource.org/licenses/mit-license.php
 */
package org.elastos;

import com.alibaba.fastjson.JSON;
import net.sf.json.JSONObject;
import org.elastos.api.SignTransaction;
import org.elastos.elaweb.ElaController;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * clark
 * <p>
 * 8/31/18
 */
public class TestHdwallet {
    static String txInfo ;
    static Map<String,String> privKeyMap = new HashMap<>();
    static {
        txInfo = "{\n" +
                "    \"result\": {\n" +
                "        \"Transactions\": [\n" +
                "            {\n" +
                "                \"UTXOInputs\": [\n" +
                "                    {\n" +
                "                        \"address\": \"EU3e23CtozdSvrtPzk9A1FeC9iGD896DdV\",\n" +
                "                        \"txid\": \"fa9bcb8b2f3a3a1e627284ad8425faf70fa64146b88a3aceac538af8bfeffd91\",\n" +
                "                        \"index\": 1\n" +
                "                    }\n" +
                "                ],\n" +
                "                \"Fee\": 100,\n" +
                "                \"Outputs\": [\n" +
                "                    {\n" +
                "                        \"amount\": 1000,\n" +
                "                        \"address\": \"EPzxJrHefvE7TCWmEGQ4rcFgxGeGBZFSHw\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"amount\": 99997800,\n" +
                "                        \"address\": \"EU3e23CtozdSvrtPzk9A1FeC9iGD896DdV\"\n" +
                "                    }\n" +
                "                ]\n" +
                "            }\n" +
                "        ]\n" +
                "    },\n" +
                "    \"status\": 200\n" +
                "}";

         privKeyMap.put("EHLhCEbwViWBPwh1VhpECzYEA7jQHZ4zLv","0D5D7566CA36BC05CFF8E3287C43977DCBB492990EA1822643656D85B3CB0226");
         privKeyMap.put("ERNEdQ3ngqjf15SsAhW8ByJQt8HEhDfuRL","E763239857B390502289CF75FF06EEEDC3252A302C50E1CBB7E5FAC8A703486F");
         privKeyMap.put("EU3e23CtozdSvrtPzk9A1FeC9iGD896DdV","DFE88FE877CD15EDFCFA2125158618BB5CB76C9465B87D0B339B735FF7F59E61");

    }
    @Test
    public void TestGenRawTx() {
        try {
            Map m = (Map) JSON.parse(txInfo);
            m = (Map) m.get("result");
            List txList = (List) m.get("Transactions");
            Map tx = (Map) txList.get(0);
            List<Map> inputs = (List)tx.get("UTXOInputs");
            for(int i=0;i<inputs.size();i++){
                Map input = inputs.get(i);
                String pubAddr = (String) input.get("address");
                input.put("privateKey", privKeyMap.get(pubAddr));
            }
            String rawTx = SignTransaction.genRawTransaction(JSONObject.fromObject(m));
            System.out.println(rawTx);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
