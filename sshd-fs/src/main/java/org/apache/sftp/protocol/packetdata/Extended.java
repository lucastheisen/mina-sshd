package org.apache.sftp.protocol.packetdata;


import org.apache.sftp.protocol.PacketData;
import org.apache.sftp.protocol.Request;
import org.apache.sftp.protocol.Response;


public interface Extended<T, S extends Response<S>> extends PacketData<T>, Request<T, S> {
    public String getName();
}
