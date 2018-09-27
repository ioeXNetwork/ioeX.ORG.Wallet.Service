package org.elastos.entity;

import java.util.List;

/**
 * 
 * @author clark
 * May 23, 2018 10:48:16 AM
 */
public class TransferParamEntity<T> {
	private String senderAddr;
	private String senderPrivateKey;
	private String memo;
	private T receiver;
//    private List<String> rcvAddrList;
//    private List<Double> rcvAmtList;
//    private Double totalAmt;

//    public List<String> getRcvAddrList() {
//        return rcvAddrList;
//    }
//
//    public void setRcvAddrList(List<String> rcvAddrList) {
//        this.rcvAddrList = rcvAddrList;
//    }
//
//    public List<Double> getRcvAmtList() {
//        return rcvAmtList;
//    }
//
//    public void setRcvAmtList(List<Double> rcvAmtList) {
//        this.rcvAmtList = rcvAmtList;
//    }
//
//    public Double getTotalAmt() {
//        return totalAmt;
//    }
//
//    public void setTotalAmt(Double totalAmt) {
//        this.totalAmt = totalAmt;
//    }

    public String getSenderAddr() {
        return senderAddr;
    }

    public void setSenderAddr(String senderAddr) {
        this.senderAddr = senderAddr;
    }

    public String getSenderPrivateKey() {
        return senderPrivateKey;
    }

    public void setSenderPrivateKey(String senderPrivateKey) {
        this.senderPrivateKey = senderPrivateKey;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public T getReceiver() {
        return receiver;
    }

    public void setReceiver(T receiver) {
        this.receiver = receiver;
    }
}
