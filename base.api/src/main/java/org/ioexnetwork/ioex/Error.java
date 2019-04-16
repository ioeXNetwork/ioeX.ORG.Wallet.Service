package org.ioexnetwork.ioex;


public class Error {
    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    private int errCode;
    private String errMsg;
    public Error(int errCode,String errMsg){
        this.errCode = errCode;
        this.errMsg = errMsg;
    }
}
