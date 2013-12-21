package org.apache.sshd.sftp.client.packetdata;


import org.apache.sshd.sftp.Request;


public interface Read extends BaseHandleOffset<Read>, Request<Read, Data> {
    public int getLength();
    
    public Read setLength( int length );
}
