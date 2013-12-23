package org.apache.sftp.protocol.packetdata;


import org.apache.sftp.protocol.Request;


public interface Read extends BaseHandleOffset<Read>, Request<Read, Data> {
    public int getLength();
    
    public Read setLength( int length );
}
