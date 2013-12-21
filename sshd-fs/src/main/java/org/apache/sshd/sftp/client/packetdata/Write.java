package org.apache.sshd.sftp.client.packetdata;


import org.apache.sshd.sftp.Request;


public interface Write extends BaseHandleOffset<Write>, Request<Write, Status> {
    public byte[] getData();

    public Write setData( byte[] data );
}
