package org.ioexnetwork.ioex;

import java.io.DataOutputStream;
import java.io.IOException;


public class Uint256 {

    final static int UINT256SIZE = 32;
    //uint
    byte[] Uint256 = new byte[UINT256SIZE];

    public Uint256(byte[] b){
        System.arraycopy(b,0,Uint256,0,32);
    }

    public void Serialize(DataOutputStream o) throws IOException {
        o.write(this.Uint256);
    }

}
