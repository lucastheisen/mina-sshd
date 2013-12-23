package org.apache.sftp.protocol.packetdata;


import java.util.Map;


import org.apache.sftp.protocol.PacketData;


public interface BaseVersion<T> extends PacketData<T> {
    public T addExtension( String name, String data );

    public Map<String, String> getExtensions();

    public int getVersion();

    public T setVersion( int version );

}
