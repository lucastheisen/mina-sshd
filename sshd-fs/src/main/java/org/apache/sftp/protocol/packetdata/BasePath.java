package org.apache.sftp.protocol.packetdata;


import org.apache.sftp.protocol.PacketData;


public interface BasePath<T> extends PacketData<T> {
    public String getPath();

    public T setPath( String path );
}
