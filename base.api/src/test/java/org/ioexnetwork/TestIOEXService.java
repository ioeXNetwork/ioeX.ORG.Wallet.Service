/**
 * Copyright (c) 2017-2018 The Elastos Developers
 * <p>
 * Distributed under the MIT software license, see the accompanying file
 * LICENSE or https://opensource.org/licenses/mit-license.php
 */
package org.ioexnetwork;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import net.sf.json.JSONObject;
import org.ioexnetwork.api.Basic;
import org.ioexnetwork.ioex.IOEX;
import org.ioexnetwork.ioex.Util;
import org.ioexnetwork.ioexweb.IOEXController;
import org.ioexnetwork.entity.ReturnMsgEntity;
import org.ioexnetwork.entity.SignDataEntity;
import org.ioexnetwork.service.IOEXService;
import org.ioexnetwork.util.HttpKit;
import org.ioexnetwork.util.JsonUtil;
import org.ioexnetwork.util.ioex.IOEXKit;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.xml.bind.DatatypeConverter;
import javax.xml.crypto.Data;
import java.util.Map;

/**
 * clark
 * <p>
 * 9/20/18
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TestIOEXService {

    @Autowired
    private IOEXService elaService ;

    @Test
    public void test(){
        String result = "{\"Desc\":\"Success\",\"Error\":0,\"Result\":{\"Hash\":\"41c6a2b46bc0c839c13cda0b42bbecacaf6051bd9ee25e6e72220f6e6dc3cf00\",\"Height\":10,\"Transactions\":[\"4f8365ce16ad5c82fee6689ab8a569d907b1d2e25424efdb75a9a23e7b704d25\"]}}";
        ReturnMsgEntity.IOEXReturnMsg retMsg = JsonUtil.jsonStr2Entity(result,ReturnMsgEntity.IOEXReturnMsg.class);

        System.out.println(JSON.toJSONString(new ReturnMsgEntity().setResult(retMsg.getResult()).setStatus(111l)));

    }

    @Test
    public void test01(){

        String str = "{\"Hash\":\"41c6a2b46bc0c839c13cda0b42bbecacaf6051bd9ee25e6e72220f6e6dc3cf00\",\"Height\":10,\"Transactions\":[\"4f8365ce16ad5c82fee6689ab8a569d907b1d2e25424efdb75a9a23e7b704d25\"]}";

        Map<String,String> result = (Map<String,String>)JSON.parse(str);

        System.out.println(JSON.toJSONString(result));


    }

    @Test
    public void test02() throws Exception{
        JSONObject param = new JSONObject();
        String privKey = IOEX.getPrivateKey();
        String did = IOEX.getIdentityIDFromPrivate(privKey);
        System.out.println(did);
    }

    @Test
    public void test_sign()throws Exception{
        String msg = "我们都是中国人";
        String privateKey = "0D5D7566CA36BC05CFF8E3287C43977DCBB492990EA1822643656D85B3CB0226";
        SignDataEntity entity = new SignDataEntity();
        entity.setMsg(msg); entity.setPrivateKey(privateKey);
        System.out.println(elaService.sign(entity));
    }

    @Test
    public void test_verify(){
        String data = "{\"result\":{\"msg\":\"E68891E4BBACE983BDE698AFE4B8ADE59BBDE4BABA\",\"pub\":\"02C3F59F337814C6715BBE684EC525B9A3CFCE55D9DEEC53E1EDDB0B352DBB4A54\",\"sig\":\"543EC5E2DC93308A5D4D76B38584D2F807E57389F4E563BA04571F64759766DDC0C0A547748A41A20DBF71FA7DF177D203EADEC0956FB45AC286895837369CAB\"},\"status\":200}";
        SignDataEntity entity = new SignDataEntity();
        entity.setPub("02C3F59F337814C6715BBE684EC525B9A3CFCE55D9DEEC53E1EDDB0B352DBB4A54");
        entity.setMsg("E68891E4BBACE983BDE698AFE4B8ADE59BBDE4BABA");
        entity.setSig("543EC5E2DC93308A5D4D76B38584D2F807E57389F4E563BA04571F64759766DDC0C0A547748A41A20DBF71FA7DF177D203EADEC0956FB45AC286895837369CAB");
        System.out.println(elaService.verify(entity));

    }

    @Test
    public void testMemo() throws Exception{
        String data = "617366e6b58be8af95";
        System.out.println(new String(DatatypeConverter.parseHexBinary(data),"utf-8"));
    }

    @Test
    public void testCodeToAddr(){
        String hexStr = "21036f9a2f47ce0ee6472c97eeec96407c70db6a3727e5427c45468693a034b21e92ac";
        byte[] program = DatatypeConverter.parseHexBinary(hexStr);
        System.out.println(Util.ToAddress(Util.ToCodeHash(program,1)));
    }

    @Test
    public void testCal(){
        double smAmt = 0.089698;
        System.out.println(Math.round((smAmt + (0.0001 * 2)) * 100000000));
    }

    @Test
    public void testWallets(){
        String privKey = IOEX.getPrivateKey();
        String addresss = IOEX.getAddressFromPrivate(privKey);
        String did = IOEX.getIdentityIDFromPrivate(privKey);
        System.out.println(addresss + "  " + did);
    }

    @Test
    public void testCheckAddr(){
        System.out.println(IOEXKit.checkAddress("EHLhCEbwViWBPwh1VhpECzYEA7jQHZ4zLv"));
    }


}
