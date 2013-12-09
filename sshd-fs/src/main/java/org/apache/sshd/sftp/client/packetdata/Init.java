package org.apache.sshd.sftp.client.packetdata;


import java.util.Map;


import org.apache.sshd.sftp.PacketData;


public interface Init extends PacketData {

    public abstract Init addExtension( String name, String data );

    public abstract Map<String, String> getExtensions();

    public abstract int getVersion();

    public abstract Init setVersion( int version );

}