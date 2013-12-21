package org.apache.sshd.sftp.client.packetdata;


import org.apache.sshd.sftp.PacketData;


public interface BasePath<T> extends PacketData<T> {
    public String getPath();

    public T setPath( String path );
}
