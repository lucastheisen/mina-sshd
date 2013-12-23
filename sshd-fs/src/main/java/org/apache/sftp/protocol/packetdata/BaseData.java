package org.apache.sftp.protocol.packetdata;


import org.apache.sftp.protocol.PacketData;


public interface BaseData<T> extends PacketData<T> {
    public byte[] getData();

    public T setData( byte[] data );
}
