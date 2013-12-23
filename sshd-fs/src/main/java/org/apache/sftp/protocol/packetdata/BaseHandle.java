package org.apache.sftp.protocol.packetdata;


import org.apache.sftp.protocol.PacketData;


public interface BaseHandle<T> extends PacketData<T> {
    public byte[] getHandle();

    public T setHandle( byte[] handle );

    public T setHandle( Handle handle );
}
