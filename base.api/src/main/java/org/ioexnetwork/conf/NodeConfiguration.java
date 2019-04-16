/**
 * Copyright (c) 2017-2018 The Elastos Developers
 * <p>
 * Distributed under the MIT software license, see the accompanying file
 * LICENSE or https://opensource.org/licenses/mit-license.php
 */
package org.ioexnetwork.conf;

import org.ioexnetwork.entity.ChainType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@ConfigurationProperties("node")
public class NodeConfiguration {
    private String prefix              ;
    private String connectionCount     ;
    private String state               ;
    private String blockTxByHeight     ;
    private String blockByHeight       ;
    private String blockByhash         ;
    private String blockHeight         ;
    private String blockHash           ;
    private String transaction         ;
    private String asset               ;
    private String balanceByAddr       ;
    private String balanceByAsset      ;
    private String utxoByAsset         ;
    private String utxoByAddr          ;
    private String sendRawTransaction  ;
    private String transactionPool     ;

    public String getPrefix() {
        return prefix;
    }

    public String getConnectionCount(ChainType type) {
        return prefix + connectionCount;
    }

    public String getState(ChainType type) {
        return prefix + state;
    }

    public String getBlockTxByHeight(ChainType type) {
        return prefix + blockTxByHeight;
    }

    public String getBlockByHeight(ChainType type) {
        return prefix + blockByHeight;
    }

    public String getBlockByhash(ChainType type) {
        return prefix + blockByhash;
    }

    public String getBlockHeight(ChainType type) {
        return prefix + blockHeight;
    }

    public String getBlockHash(ChainType type) {
        return prefix + blockHash;
    }

    public String getTransaction(ChainType type) {
        return prefix + transaction;
    }

    public String getAsset(ChainType type) {
        return prefix + asset;
    }

    public String getBalanceByAddr(ChainType type) {
        return prefix + balanceByAddr;
    }

    public String getBalanceByAsset(ChainType type) {
        return prefix + balanceByAsset;
    }

    public String getUtxoByAsset(ChainType type) {
        return prefix + utxoByAsset;
    }

    public String getUtxoByAddr(ChainType type) {
        return prefix + utxoByAddr;
    }

    public String sendRawTransaction(ChainType type) {
        return prefix + sendRawTransaction;
    }

    public String getTransactionPool(ChainType type) {
        return prefix + transactionPool;
    }
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setConnectionCount(String connectionCount) {
        this.connectionCount = connectionCount;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setBlockTxByHeight(String blockTxByHeight) {
        this.blockTxByHeight = blockTxByHeight;
    }

    public void setBlockByHeight(String blockByHeight) {
        this.blockByHeight = blockByHeight;
    }

    public void setBlockByhash(String blockByhash) {
        this.blockByhash = blockByhash;
    }

    public void setBlockHeight(String blockHeight) {
        this.blockHeight = blockHeight;
    }

    public void setBlockHash(String blockHash) {
        this.blockHash = blockHash;
    }

    public void setTransaction(String transaction) {
        this.transaction = transaction;
    }

    public void setAsset(String asset) {
        this.asset = asset;
    }

    public void setBalanceByAddr(String balanceByAddr) {
        this.balanceByAddr = balanceByAddr;
    }

    public void setBalanceByAsset(String balanceByAsset) {
        this.balanceByAsset = balanceByAsset;
    }

    public void setUtxoByAsset(String utxoByAsset) {
        this.utxoByAsset = utxoByAsset;
    }

    public void setUtxoByAddr(String utxoByAddr) {
        this.utxoByAddr = utxoByAddr;
    }

    public void setSendRawTransaction(String sendRawTransaction) {
        this.sendRawTransaction = sendRawTransaction;
    }

    public void setTransactionPool(String transactionPool) {
        this.transactionPool = transactionPool;
    }
}
