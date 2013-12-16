package org.apache.sshd.sftp.client.packetdata;


import java.util.Map;


import org.apache.sshd.sftp.PacketData;


public interface Init extends PacketData<Init> {

    public Init addExtension( String name, String data );

    public abstract Map<String, String> getExtensions();

    public abstract int getVersion();

    public Init setVersion( int version );

}