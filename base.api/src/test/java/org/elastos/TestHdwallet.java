/**
 * Copyright (c) 2017-2018 The Elastos Developers
 * <p>
 * Distributed under the MIT software license, see the accompanying file
 * LICENSE or https://opensource.org/licenses/mit-license.php
 */
package org.elastos;

import com.alibaba.fastjson.JSON;
import net.sf.json.JSONObject;
import org.elastos.api.SingleSignTransaction;
import org.elastos.elaweb.ElaController;
import org.junit.Ignore;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
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
                "                        \"address\": \"EbxU18T3M9ufnrkRY7NLt6sKyckDW4VAsA\",\n" +
                "                        \"txid\": \"9e9cd9fdf8255fb4e6fa3e7825712349fb6644baae23cf9ef9dd55730d6aecba\",\n" +
                "                        \"index\": 1\n" +
                "                    }\n" +
                "                ],\n" +
                "                \"Fee\": 100,\n" +
                "                \"Outputs\": [\n" +
                "                    {\n" +
                "                        \"amount\": 1201,\n" +
                "                        \"address\": \"ELag7vYvKcUBVKJkWosBQw73HSx8madjcP\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"amount\": 34220693,\n" +
                "                        \"address\": \"EbxU18T3M9ufnrkRY7NLt6sKyckDW4VAsA\"\n" +
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
         privKeyMap.put("EXr8pYTR5Z56Ni9Vg9r5UnNg2MCQ6enm93","729E2BB0AEEC048FF9DC7996D394889687BF76AFA832F07E011AA5A3BE270410");
         privKeyMap.put("EbxU18T3M9ufnrkRY7NLt6sKyckDW4VAsA","C740869D015E674362B1F441E3EDBE1CBCF4FE8B709AA1A77E5CCA2C92BAF99D");
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
            String rawTx = SingleSignTransaction.genRawTransaction(JSONObject.fromObject(m));
            System.out.println(rawTx);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void TestMnemonic() throws Exception{
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("english");
        System.out.println(is);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        DataOutputStream o = new DataOutputStream(os);
        long l = 9899999900l;
        o.writeLong(l);

        System.out.print(111);
    }
}
