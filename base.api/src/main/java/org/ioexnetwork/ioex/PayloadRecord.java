package org.ioexnetwork.ioex;

import java.io.DataOutputStream;
import java.io.IOException;

import static org.ioexnetwork.ioex.Util.WriteVarUint;
public class PayloadRecord {
    String RecordType;
    byte[] RecordData;

    public void Serialize(DataOutputStream o) throws IOException {
        WriteVarUint(o, this.RecordType.length());
        o.write(this.RecordType.getBytes());
        Util.WriteVarBytes(o,this.RecordData);
    }

    public PayloadRecord(String type, String data){
        this.RecordType = type;
        this.RecordData = data.getBytes();
    }
}
