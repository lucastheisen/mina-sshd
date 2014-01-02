package org.apache.sftp.protocol.packetdata;


import org.apache.sftp.protocol.PacketData;


public interface Implementation<T extends PacketData<T>> {
    public Class<? extends T> getImplementationType();

    public Class<T> getInterfaceType();
}
