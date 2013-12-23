package org.apache.sftp.protocol.packetdata;


import org.apache.sftp.protocol.Request;


public interface Write extends BaseHandleOffset<Write>, Request<Write, Status> {
    public byte[] getData();

    public Write setData( byte[] data );
}
