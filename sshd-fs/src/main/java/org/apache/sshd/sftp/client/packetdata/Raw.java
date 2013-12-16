package org.apache.sshd.sftp.client.packetdata;


import org.apache.sshd.sftp.Request;
import org.apache.sshd.sftp.Response;


public interface Raw extends Request<Raw>, Response<Raw> {
    public byte[] getBytes();

    public Raw setBytes( byte[] bytes );
}
