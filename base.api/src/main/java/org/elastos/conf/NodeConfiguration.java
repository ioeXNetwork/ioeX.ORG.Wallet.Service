/**
 * Copyright (c) 2017-2018 The Elastos Developers
 * <p>
 * Distributed under the MIT software license, see the accompanying file
 * LICENSE or https://opensource.org/licenses/mit-license.php
 */
package org.elastos.conf;

import org.elastos.entity.ChainType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 *
 * clark
 *
 * 9/3/18
 *
 *
 */
@Component
@ConfigurationProperties("node")
public class NodeConfiguration {
    private String prefix              ;
    private String didPrefix           ;
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
        if(type == ChainType.DID_SIDECHAIN){
            return didPrefix + connectionCount;
        }
        return prefix + connectionCount;
    }

    public String getState(ChainType type) {
        if(type == ChainType.DID_SIDECHAIN){
            return didPrefix + state;
        }
        return prefix + state;
    }

    public String getBlockTxByHeight(ChainType type) {
        if(type == ChainType.DID_SIDECHAIN){
            return didPrefix + blockTxByHeight;
        }
        return prefix + blockTxByHeight;
    }

    public String getBlockByHeight(ChainType type) {
        if(type == ChainType.DID_SIDECHAIN){
            return didPrefix + blockByHeight;
        }
        return prefix + blockByHeight;
    }

    public String getBlockByhash(ChainType type) {
        if(type == ChainType.DID_SIDECHAIN){
            return didPrefix + blockByhash;
        }
        return prefix + blockByhash;
    }

    public String getBlockHeight(ChainType type) {
        if(type == ChainType.DID_SIDECHAIN){
            return didPrefix + blockHeight;
        }
        return prefix + blockHeight;
    }

    public String getBlockHash(ChainType type) {
        if(type == ChainType.DID_SIDECHAIN){
            return didPrefix + blockHash;
        }
        return prefix + blockHash;
    }

    public String getTransaction(ChainType type) {
        if(type == ChainType.DID_SIDECHAIN ){
            return didPrefix + transaction;
        }
        return prefix + transaction;
    }

    public String getAsset(ChainType type) {
        if(type == ChainType.DID_SIDECHAIN){
            return didPrefix + asset;
        }
        return prefix + asset;
    }

    public String getBalanceByAddr(ChainType type) {
        if(type == ChainType.DID_SIDECHAIN){
            return didPrefix + balanceByAddr;
        }
        return prefix + balanceByAddr;
    }

    public String getBalanceByAsset(ChainType type) {
        if(type == ChainType.DID_SIDECHAIN ){
            return didPrefix + balanceByAsset;
        }
        return prefix + balanceByAsset;
    }

    public String getUtxoByAsset(ChainType type) {
        if(type == ChainType.DID_SIDECHAIN || type == ChainType.DID_MAIN_CROSS_CHAIN){
            return didPrefix + utxoByAsset;
        }
        return prefix + utxoByAsset;
    }

    public String getUtxoByAddr(ChainType type) {
        if(type == ChainType.DID_SIDECHAIN || type == ChainType.DID_MAIN_CROSS_CHAIN){
            return didPrefix + utxoByAddr;
        }
        return prefix + utxoByAddr;
    }

    public String sendRawTransaction(ChainType type) {
        if(type == ChainType.DID_SIDECHAIN || type == ChainType.DID_MAIN_CROSS_CHAIN){
            return didPrefix + sendRawTransaction;
        }
        return prefix + sendRawTransaction;
    }

    public String getTransactionPool(ChainType type) {
        if(type == ChainType.DID_SIDECHAIN){
            return didPrefix + transactionPool;
        }
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

    public String getDidPrefix() {
        return didPrefix;
    }

    public void setDidPrefix(String didPrefix) {
        this.didPrefix = didPrefix;
    }

}
