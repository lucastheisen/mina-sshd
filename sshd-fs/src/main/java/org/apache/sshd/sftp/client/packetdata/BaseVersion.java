package org.apache.sshd.sftp.client.packetdata;


import java.util.Map;


import org.apache.sshd.sftp.PacketData;


public interface BaseVersion<T> extends PacketData<T> {
    public T addExtension( String name, String data );

    public Map<String, String> getExtensions();

    public int getVersion();

    public T setVersion( int version );

}
