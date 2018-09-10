/**
 * Copyright (c) 2017-2018 The Elastos Developers
 * <p>
 * Distributed under the MIT software license, see the accompanying file
 * LICENSE or https://opensource.org/licenses/mit-license.php
 */
package org.elastos;

import com.alibaba.fastjson.JSON;
import net.sf.json.JSONObject;
import org.elastos.elaweb.ElaController;
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
                "                        \"address\": \"EHLhCEbwViWBPwh1VhpECzYEA7jQHZ4zLv\",\n" +
                "                        \"txid\": \"8ca04562c00c26a2a0669a594da61f167d226d9357ebba3069a2930e6154bc08\",\n" +
                "                        \"index\": 2\n" +
                "                    }\n" +
                "                ],\n" +
                "                \"Fee\": 100,\n" +
                "                \"Outputs\": [\n" +
                "                    {\n" +
                "                        \"amount\": 1000,\n" +
                "                        \"address\": \"EPzxJrHefvE7TCWmEGQ4rcFgxGeGBZFSHw\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"amount\": 9000,\n" +
                "                        \"address\": \"EK2x7bGyUQsgg1TVVkBCPCkjQdYCCf57fe\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"amount\": 969600,\n" +
                "                        \"address\": \"EHLhCEbwViWBPwh1VhpECzYEA7jQHZ4zLv\"\n" +
                "                    }\n" +
                "                ]\n" +
                "            }\n" +
                "        ]\n" +
                "    },\n" +
                "    \"status\": 200\n" +
                "}";

         privKeyMap.put("EHLhCEbwViWBPwh1VhpECzYEA7jQHZ4zLv","0D5D7566CA36BC05CFF8E3287C43977DCBB492990EA1822643656D85B3CB0226");
         privKeyMap.put("ERNEdQ3ngqjf15SsAhW8ByJQt8HEhDfuRL","E763239857B390502289CF75FF06EEEDC3252A302C50E1CBB7E5FAC8A703486F");
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
            String rawTx = ElaController.genRawTransaction(JSONObject.fromObject(m));
            System.out.println(rawTx);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
