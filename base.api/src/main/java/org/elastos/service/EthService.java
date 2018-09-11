/**
 * Copyright (c) 2017-2018 The Elastos Developers
 * <p>
 * Distributed under the MIT software license, see the accompanying file
 * LICENSE or https://opensource.org/licenses/mit-license.php
 */
package org.elastos.service;

import org.elastos.conf.EthConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * clark
 * <p>
 * 9/7/18
 */
@Service
public class EthService {
    @Autowired
    private EthConfiguration ethConfiguration;
    private Web3j web3j ;

    private void init(){
        if (web3j == null){
            web3j = Web3j.build(new HttpService(ethConfiguration.getInfuraAccessUrl()));
        }
    }

    /**
     * get instance of web3j
     * @return
     */
    public Web3j web3j(){
        if (web3j == null){
            init();
        }
        return web3j;
    }

    /**
     * get gas limit
     * @return
     */
    public BigInteger getGasLimit(){
        return ethConfiguration.getGasLimit();
    }

    /**
     * create a ETH wallet
     * @param password password used to generate wallet
     * @param descDir  wallet file location
     * @return wallet file name
     * @throws Exception
     */
    public String createWallet(String password,String descDir) throws Exception{
        return WalletUtils.generateNewWalletFile(
                password,
                new File(descDir));
    }

    /**
     * load credential
     * @param password wallet password
     * @param walletfile wallet full filename
     * @return
     * @throws Exception
     */
    public Credentials loadCredentials(String password,String walletfile) throws Exception{
        return WalletUtils.loadCredentials(
                password,
                walletfile);
    }

    /**
     * get nonce of sender account
     * @param address sender account
     * @param defaultBlockParameter latest | pending | earliest
     * @return
     * @throws Exception
     */
    public BigInteger getNonce(String address ,String defaultBlockParameter) throws Exception{
        if(web3j == null){
            init();
        }
        EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(
                address,
                DefaultBlockParameter.valueOf(defaultBlockParameter)).send();
        return ethGetTransactionCount.getTransactionCount();
    }

    /**
     * get the current gas price
     * @return gas price
     * @throws Exception
     */
    public BigInteger getGasPrice() throws Exception{
        if(web3j == null){
            init();
        }
        EthGasPrice ethGasPrice = web3j.ethGasPrice().send();
        return ethGasPrice.getGasPrice();
    }

    /**
     * send ETH
     * @param senderAddr
     * @param toAddr
     * @param value
     * @param credentials
     * @return
     * @throws Exception
     */
    @Deprecated
    public String sendTx(String senderAddr , String toAddr , BigInteger value,Credentials credentials) throws  Exception{
        if(web3j == null){
            init();
        }
        BigInteger nonce = getNonce(senderAddr,"latest");
        RawTransaction rawTransaction  = RawTransaction.createEtherTransaction(
                nonce, getGasPrice(), ethConfiguration.getGasLimit(), toAddr, value);
        return sendRawTx(rawTransaction,credentials);
    }


    /**
     * send ETH
     * @param toAddr
     * @param value
     * @param credentials
     * @return transaction receipt
     * @throws Exception
     */
    public TransactionReceipt sendTx(String toAddr,BigInteger value,Credentials credentials) throws Exception {
        if(web3j == null){
            init();
        }
        TransactionReceipt transactionReceipt = Transfer.sendFunds(web3j, credentials, toAddr, new BigDecimal(value), Convert.Unit.WEI).send();
        return transactionReceipt;
    }

    /**
     * send raw Transaction
     * @param rawTransaction
     * @param credentials
     * @return transaction id
     */
    private String sendRawTx(RawTransaction rawTransaction , Credentials credentials) throws Exception{
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        String hexValue = Numeric.toHexString(signedMessage);
        EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).sendAsync().get();
        String transactionHash = ethSendTransaction.getTransactionHash();
        return transactionHash;
    }
}
