package org.apache.sshd.sftp.client.packetdata;


import org.apache.sshd.sftp.PacketData;


public interface BaseData<T> extends PacketData<T> {
    public byte[] getData();

    public T setData( byte[] data );
}
