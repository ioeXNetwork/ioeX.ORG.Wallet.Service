/**
 * Copyright (c) 2017-2018 The Elastos Developers
 * <p>
 * Distributed under the MIT software license, see the accompanying file
 * LICENSE or https://opensource.org/licenses/mit-license.php
 */
package org.ioexnetwork;

import org.ioexnetwork.contract.*;
import org.ioexnetwork.service.EthService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.crypto.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;
import rx.Observable;

import javax.xml.bind.DatatypeConverter;
import java.math.BigDecimal;
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
    private String fullfileName = "/Users/clark/Desktop/UTC--2018-11-23T12-59-55.564000000Z--f4c79a07fc0533edd0410a0d8a410a628474535e.json";
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
    @Ignore
    public void testGetClientVersion() throws Exception{
        String version = web3j.web3ClientVersion().send().getWeb3ClientVersion();
        System.out.println(version);
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
    @Ignore
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
    @Ignore
    public void testSendETHV2(){
        try{
            BigInteger value = Convert.toWei("0.5", Convert.Unit.ETHER).toBigInteger();
            TransactionReceipt receipt = ethService.sendTx("0x6fb496dc489e7cbfd3e745c9e4ea281897e69ee0",value,credentials);
            System.out.println(receipt);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Test
    @Ignore
    public void testDeploySmartContractMortal() throws Exception{
        Mortal mortal = Mortal.deploy(web3j,credentials,gasPrice,gasLimit).send();
        System.out.println(mortal.getContractAddress()+ "  " + mortal.getTransactionReceipt().get());
    }

    @Test
    @Ignore
    public void testLoadSmartContractMortal() throws Exception{
        Mortal mortal = Mortal.load("0x629d7b3c3f351f31dbe0961e2b0047a126843583",web3j,credentials,gasPrice,gasLimit);
        System.out.println(mortal.isValid());
    }

    @Test
    @Ignore
    public void testDeploySmartContractGreeter() throws Exception{
        Greeter greeter = Greeter.deploy(web3j,credentials,gasPrice,gasLimit,"hello,clark").send();
        System.out.println(greeter.getContractAddress()+ "  " + greeter.getTransactionReceipt().get());
    }

    @Test
    @Ignore
    public void testLoadSmartContractGreeter() throws Exception {
        Greeter greeter = Greeter.load("0xbccf97397191ee24633c72d969aae0b155d3416b",web3j,credentials,gasPrice,gasLimit);
        String greeting = greeter.greet().send();
        System.out.println(greeting);
    }

    @Test
    @Ignore
    public void testDeploySmartContractArrays() throws Exception{
        Arrays arrays = Arrays.deploy(web3j,credentials,new BigInteger("50000000000"),gasLimit).send();
        logger.debug("contract address {}, transaction receipt {}", arrays.getContractAddress(),arrays.getTransactionReceipt());
        System.out.println(arrays);
    }

    @Test
    @Ignore
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
    @Ignore
    public void testDeploySmartContractFibonacci() throws Exception {
        Fibonacci fibonacci = Fibonacci.deploy(web3j,credentials,gasPrice,gasLimit).send();
        System.out.println(fibonacci.getContractAddress() + " " + fibonacci.getTransactionReceipt());
    }

    @Test
    @Ignore
    public void testLoadSmartContractFibonacci() throws Exception {
        Fibonacci fibonacci = Fibonacci.load("0xb372f7c6c0e4e4b529a7ee5c1510571847118cdd",web3j,credentials,gasPrice,gasLimit);
//        BigInteger bigInteger = fibonacci.fibonacci(new BigInteger("10")).send();
//        System.out.println(bigInteger.toString());
        BigInteger value = fibonacci.fibonacci(new BigInteger("3")).sendAsync().get();
        System.out.println(value);
    }


    @Test
    @Ignore
    public void testDeploySmartContractHumanStandardToken() throws Exception {
        HumanStandardToken token = HumanStandardToken.deploy(web3j,credentials,gasPrice,gasLimit,new BigInteger("1000"),"TEST_FKCN",new BigInteger("18"),"TF").sendAsync().get();
        System.out.println(token.getContractAddress());
        //0x00A6e48F3f4217AaD6feE557bd8B491A0B9e06b9
    }

    @Test
    @Ignore
    public void testLoadSmartContractHumanStandardToken() throws Exception {

        HumanStandardToken token = HumanStandardToken.load("0x00a6e48f3f4217aad6fee557bd8b491a0b9e06b9",web3j,credentials,gasPrice,gasLimit);

        TransactionReceipt tr = token.transfer("fdbf471435042903392a4fccc8c4ddac882d2294",new BigInteger("100")).sendAsync().get();

        List<HumanStandardToken.TransferEventResponse> response = token.getTransferEvents(tr);

        System.out.println(response.get(0));
    }

    @Test
    @Ignore
    public void testDeploySmartContractSimpleStorage() throws Exception{

        SimpleStorage simpleStorage = SimpleStorage.deploy(web3j,credentials,gasPrice,gasLimit).sendAsync().get();

        System.out.println(simpleStorage.getContractAddress());
    }

    @Test
    @Ignore
    public void testLoadSmartContractSimpleStorage() throws Exception {

        SimpleStorage simpleStorage = SimpleStorage.load("0xafc459e71f9ba894cbe21b05694392c053c551b6",web3j,credentials,gasPrice,gasLimit);

        TransactionReceipt tr = simpleStorage.set(new BigInteger("99999")).sendAsync().get();

        System.out.println(tr.getTransactionHash());

        System.out.println(simpleStorage.get().sendAsync().get());
    }

    @Test
    @Ignore
    public void testDeploySmartContractShipIt() throws Exception{

        ShipIt shipIt = ShipIt.deploy(web3j,credentials,gasPrice,gasLimit).sendAsync().get();

        System.out.println(shipIt.getContractAddress());
    }

    @Test
    @Ignore
    public void testLoadSmartContractShipIt() throws Exception {

        ShipIt shipIt = ShipIt.load("0x75a21e14bad2dfb176a5d3afa05b12ebd02c4e54",web3j,credentials,gasPrice,gasLimit);

        System.out.println(shipIt.shipments("b54627981f77fbf8a61282a66429c2fd3c303208").sendAsync().get());

    }

    @Test
    @Ignore
    public void testDeploySmartContractBallot() throws Exception {
        List<byte[]> proposals = new ArrayList<>();
        byte[] first = stringToBytes32("clark");
        proposals.add((first));
        byte[] second = stringToBytes32("bob");
        proposals.add(second);
        byte[] third = stringToBytes32("alice");
        proposals.add(third);
        Ballot ballot = Ballot.deploy(web3j,credentials,gasPrice,gasLimit,proposals).sendAsync().get();
        System.out.println(ballot.getContractAddress());
    }

    @Test
    @Ignore
    public void test() throws Exception{
//        byte[] third = stringToBytes32("alice");
//        System.out.println(new String(third));

//        byte[] first = stringToBytes32("bob");
//        System.out.println(Numeric.toHexString(first));

//        String str = new String(DatatypeConverter.parseHexBinary("0x626f620000000000000000000000000000000000000000000000000000000000"));
//        System.out.println(str);
        //1536908263 1536908332
//        System.out.println(System.currentTimeMillis()/1000);

        System.out.println(web3j.ethGetBalance("0xb54627981f77fbf8a61282a66429c2fd3c303208", DefaultBlockParameter.valueOf("latest")).sendAsync().get().getBalance());
    }

    public static byte[] stringToBytes32(String string) {
        byte[] byteValue = string.getBytes();
        byte[] byteValueLen32 = new byte[32];
        System.arraycopy(byteValue, 0, byteValueLen32, 0, byteValue.length);
        return byteValueLen32;
    }

    @Test
    @Ignore
    public void testLoadSmartContractBallot() throws Exception{
        Ballot ballot = Ballot.load("0x20Ad9184672c07c1B8215E013307C6944BECa4A6",web3j,credentials,gasPrice,gasLimit);
//        TransactionReceipt tr = ballot.giveRightToVote("0xfdbf471435042903392a4fccc8c4ddac882d2294").sendAsync().get();
//        System.out.println(tr.getTransactionHash());
        TransactionReceipt tr = ballot.vote(new BigInteger("1")).sendAsync().get();
        System.out.println(tr.getTransactionHash());
    }

    @Test
    @Ignore
    public void testDeloySmartContractCurrentTimestamp() throws Exception{

        CurrentTimestamp ct = CurrentTimestamp.deploy(web3j,credentials,gasPrice,gasLimit).sendAsync().get();

        System.out.println(ct.getContractAddress());

    }

    @Test
    @Ignore
    public void testLoadSmartContractCurrentTimestamp() throws Exception{

        CurrentTimestamp ct = CurrentTimestamp.load("0x22cda3d21998a0928095bb6387ec6b529edcacfe",web3j,credentials,gasPrice,gasLimit);
        System.out.println(ct.getCurrTimestamp().send() + "  " + ct.testString().sendAsync().get());

    }


    @Test
    @Ignore
    public void testDeploySmartContractIOEX() throws Exception{
        IOEX ioex = IOEX.deploy(web3j,credentials,gasPrice,gasLimit,new BigInteger("33000000"),"ioex","IOEX").sendAsync().get();
        System.out.println(ioex.getContractAddress());
    }

    @Test
    @Ignore
    public void testLoadSmartContractIOEX() throws Exception{

        IOEX ioex = IOEX.load("0xf3a35e9d4e21bead3f4ec523abab385699d879a4",web3j,credentials,gasPrice,gasLimit);
        EthFilter filter = new EthFilter(new DefaultBlockParameterNumber(10),
                new DefaultBlockParameterNumber(100),"0x6fb496dc489e7cbfd3e745c9e4ea281897e69ee0");
        Observable<IOEX.TransferEventResponse> observable = ioex.transferEventObservable(filter);
        observable.take(0);
        BigInteger amt = new BigInteger("1000000000000000000");

        TransactionReceipt tr = ioex.transfer("0x6fb496dc489e7cbfd3e745c9e4ea281897e69ee0",amt).sendAsync().get();

        System.out.println(tr.getTransactionHash());
    }

    @Test
    public void testSign(){

        byte[] msg = "helloworld".getBytes();
        ECKeyPair pair = credentials.getEcKeyPair();
        System.out.println(pair.getPublicKey());
        Sign.SignatureData data = Sign.signMessage(msg,pair);

        ECDSASignature signature = new ECDSASignature(new BigInteger(data.getR()),new BigInteger(data.getS()));
        BigInteger pubNum = Sign.recoverFromSignature(((int)(data.getV()))-27,signature,Hash.sha3(msg));
        String pubStr = DatatypeConverter.printHexBinary(pubNum.toByteArray());
        System.out.println(pubStr);
    }
}
