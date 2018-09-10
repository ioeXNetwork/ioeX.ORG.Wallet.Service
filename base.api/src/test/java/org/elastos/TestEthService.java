/**
 * Copyright (c) 2017-2018 The Elastos Developers
 * <p>
 * Distributed under the MIT software license, see the accompanying file
 * LICENSE or https://opensource.org/licenses/mit-license.php
 */
package org.elastos;

import org.elastos.service.EthService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Convert;

import java.math.BigInteger;


/**
 * JSON-RPC 2.0 Integration Tests.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TestEthService {

    @Autowired
    private EthService ethService;
    private String password = "123456";
    private String fullfileName = "/Users/clark/Desktop/UTC--2018-09-07T09-29-27.621000000Z--fdbf471435042903392a4fccc8c4ddac882d2294.json";

    @Test
    public void testSendETHV1(){
        try{
            BigInteger value = Convert.toWei("10", Convert.Unit.ETHER).toBigInteger();
            Credentials credentials = ethService.loadCredentials(password,fullfileName);
            String txid = ethService.sendTx("0xFdBF471435042903392a4FcCc8c4ddAc882D2294",
                    "0xb54627981f77fbf8a61282a66429c2fd3c303208",value,credentials);
            System.out.println(txid);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Test
    public void testSendETHV2(){
        try{
            BigInteger value = Convert.toWei("0.01", Convert.Unit.ETHER).toBigInteger();
            Credentials credentials = ethService.loadCredentials(password,fullfileName);
            TransactionReceipt receipt = ethService.sendTx("0xb54627981f77fbf8a61282a66429c2fd3c303208",value,credentials);
            System.out.println(receipt);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }


    @Test
    public void testGenAddr(){
        try{
            String fileName = ethService.createWallet("123456","/Users/clark/Desktop");
            System.out.println(fileName);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    @Test
    public void testAll(){
        BigInteger value = Convert.toWei("0.1", Convert.Unit.ETHER).toBigInteger();
        System.out.println(value);
    }


}
