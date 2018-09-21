/**
 * Copyright (c) 2017-2018 The Elastos Developers
 * <p>
 * Distributed under the MIT software license, see the accompanying file
 * LICENSE or https://opensource.org/licenses/mit-license.php
 */
package org.elastos;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import org.elastos.entity.ReturnMsgEntity;
import org.elastos.util.HttpKit;
import org.elastos.util.JsonUtil;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Map;

/**
 * clark
 * <p>
 * 9/20/18
 */
public class TestElaService {

    @Test
    @Ignore
    public void test(){
        String result = "{\"Desc\":\"Success\",\"Error\":0,\"Result\":{\"Hash\":\"41c6a2b46bc0c839c13cda0b42bbecacaf6051bd9ee25e6e72220f6e6dc3cf00\",\"Height\":10,\"Transactions\":[\"4f8365ce16ad5c82fee6689ab8a569d907b1d2e25424efdb75a9a23e7b704d25\"]}}";
        ReturnMsgEntity.ELAReturnMsg retMsg = JsonUtil.jsonStr2Entity(result,ReturnMsgEntity.ELAReturnMsg.class);

        System.out.println(JSON.toJSONString(new ReturnMsgEntity().setResult(retMsg.getResult()).setStatus(111l)));

    }

    @Test
    @Ignore
    public void test01(){

        String str = "{\"Hash\":\"41c6a2b46bc0c839c13cda0b42bbecacaf6051bd9ee25e6e72220f6e6dc3cf00\",\"Height\":10,\"Transactions\":[\"4f8365ce16ad5c82fee6689ab8a569d907b1d2e25424efdb75a9a23e7b704d25\"]}";

        Map<String,String> result = (Map<String,String>)JSON.parse(str);

        System.out.println(JSON.toJSONString(result));


    }
}
