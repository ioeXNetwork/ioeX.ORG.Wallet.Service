/**
 * Copyright (c) 2017-2018 The Elastos Developers
 * <p>
 * Distributed under the MIT software license, see the accompanying file
 * LICENSE or https://opensource.org/licenses/mit-license.php
 */
package org.elastos;

import org.elastos.contract.*;
import org.elastos.service.EthService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Convert;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


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
//    private String fullfileName = "/Users/clark/Desktop/UTC--2018-09-07T09-29-27.621000000Z--fdbf471435042903392a4fccc8c4ddac882d2294.json";
    private String fullfileName = "/Users/clark/Desktop/UTC--2018-09-10T06-14-45.698000000Z--b54627981f77fbf8a61282a66429c2fd3c303208.json";
    private Credentials credentials;
    private Web3j web3j;
    private BigInteger gasPrice = new BigInteger("22000000000");
    private BigInteger gasLimit;
    private static Logger logger = LoggerFactory.getLogger(TestEthService.class);
    @Before
    public void init() throws Exception{
        credentials = ethService.loadCredentials(password,fullfileName);
        web3j = ethService.web3j();
        gasLimit = ethService.getGasLimit();
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
    public void testSendETHV1(){
        try{
            BigInteger value = Convert.toWei("10", Convert.Unit.ETHER).toBigInteger();
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
            TransactionReceipt receipt = ethService.sendTx("0xb54627981f77fbf8a61282a66429c2fd3c303208",value,credentials);
            System.out.println(receipt);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Test
    public void testDeploySmartContractMortal() throws Exception{
        Mortal mortal = Mortal.deploy(web3j,credentials,gasPrice,gasLimit).send();
        System.out.println(mortal.getContractAddress()+ "  " + mortal.getTransactionReceipt().get());
    }

    @Test
    public void testLoadSmartContractMortal() throws Exception{
        Mortal mortal = Mortal.load("0x629d7b3c3f351f31dbe0961e2b0047a126843583",web3j,credentials,gasPrice,gasLimit);
        System.out.println(mortal.isValid());
    }

    @Test
    public void testDeploySmartContractGreeter() throws Exception{
        Greeter greeter = Greeter.deploy(web3j,credentials,gasPrice,gasLimit,"hello,clark").send();
        System.out.println(greeter.getContractAddress()+ "  " + greeter.getTransactionReceipt().get());
    }

    @Test
    public void testLoadSmartContractGreeter() throws Exception {
        Greeter greeter = Greeter.load("0xbccf97397191ee24633c72d969aae0b155d3416b",web3j,credentials,gasPrice,gasLimit);
        String greeting = greeter.greet().send();
        System.out.println(greeting);
    }

    @Test
    public void testDeploySmartContractArrays() throws Exception{
        Arrays arrays = Arrays.deploy(web3j,credentials,new BigInteger("50000000000"),gasLimit).send();
        logger.debug("contract address {}, transaction receipt {}", arrays.getContractAddress(),arrays.getTransactionReceipt());
        System.out.println(arrays);
    }

    @Test
    public void testLoadSmartContractArrays() throws Exception {
        Arrays arrays = Arrays.load("0x7d621b7ab2a3F9aE87f3831e1E300Bc21D3e614C",web3j,credentials,gasPrice,gasLimit);
        List<BigInteger> origin = new ArrayList<BigInteger>();
        origin.add(new BigInteger("2"));
        origin.add(new BigInteger("10"));
        origin.add(new BigInteger("8"));
        origin.add(new BigInteger("9"));
        origin.add(new BigInteger("12"));
        origin.add(new BigInteger("80"));
        origin.add(new BigInteger("90"));
        origin.add(new BigInteger("100"));
        origin.add(new BigInteger("81"));
        origin.add(new BigInteger("91"));

        TransactionReceipt tr = arrays.fixedReverse(origin).sendAsync().get();
        System.out.println(tr);
        System.out.println(origin);

    }

    @Test
    public void testDeploySmartContractFibonacci() throws Exception {
        Fibonacci fibonacci = Fibonacci.deploy(web3j,credentials,gasPrice,gasLimit).send();
        System.out.println(fibonacci.getContractAddress() + " " + fibonacci.getTransactionReceipt());
    }

    @Test
    public void testLoadSmartContractFibonacci() throws Exception {
        Fibonacci fibonacci = Fibonacci.load("0xb372f7c6c0e4e4b529a7ee5c1510571847118cdd",web3j,credentials,gasPrice,gasLimit);
//        BigInteger bigInteger = fibonacci.fibonacci(new BigInteger("10")).send();
//        System.out.println(bigInteger.toString());
        BigInteger value = fibonacci.fibonacci(new BigInteger("3")).sendAsync().get();
        System.out.println(value);
    }


    @Test
    public void testDeploySmartContractHumanStandardToken() throws Exception {
        HumanStandardToken token = HumanStandardToken.deploy(web3j,credentials,gasPrice,gasLimit,new BigInteger("1000"),"TEST_FKCN",new BigInteger("18"),"TF").sendAsync().get();
        System.out.println(token.getContractAddress());
        //0x00A6e48F3f4217AaD6feE557bd8B491A0B9e06b9
    }

    @Test
    public void testLoadSmartContractHumanStandardToken() throws Exception {

        HumanStandardToken token = HumanStandardToken.load("0x00a6e48f3f4217aad6fee557bd8b491a0b9e06b9",web3j,credentials,gasPrice,gasLimit);

        TransactionReceipt tr = token.transfer("fdbf471435042903392a4fccc8c4ddac882d2294",new BigInteger("100")).sendAsync().get();

        List<HumanStandardToken.TransferEventResponse> response = token.getTransferEvents(tr);

        System.out.println(response.get(0));
    }
}
