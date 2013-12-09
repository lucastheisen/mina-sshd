package org.apache.sshd.sftp.client.packetdata;


import java.util.Map;


import org.apache.sshd.sftp.PacketData;


/**
 * Not exactly Init, but effectively the same. So for now, extend it and
 * override the one change.
 * 
 * @author Lucas Theisen
 */
public interface Version extends PacketData {
    public Version addExtension( String name, String data );

    public Map<String, String> getExtensions();

    public int getVersion();

    public Version setVersion( int version );
}
