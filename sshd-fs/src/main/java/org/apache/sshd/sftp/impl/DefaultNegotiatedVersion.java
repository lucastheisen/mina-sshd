package org.apache.sshd.sftp.impl;


import java.util.Map;


import org.apache.sshd.sftp.NegotiatedVersion;
import org.apache.sshd.sftp.client.packetdata.Init;
import org.apache.sshd.sftp.client.packetdata.Version;


public class DefaultNegotiatedVersion implements NegotiatedVersion {
    private Init initPacketData;
    private int version;
    private Version versionPacketData;

    public DefaultNegotiatedVersion( Init init, Version version ) {
        this.initPacketData = init;
        this.versionPacketData = version;
        this.version = Math.min( init.getVersion(), version.getVersion() );
    }

    @Override
    public Map<String, String> clientExtensions() {
        return initPacketData.getExtensions();
    }

    @Override
    public Map<String, String> serverExtensions() {
        return versionPacketData.getExtensions();
    }

    @Override
    public int version() {
        return version;
    }

    @Override
    public String toString() {
        return new StringBuilder( "{'negotiated':" )
                .append( version )
                .append( ",'init':" ).append( initPacketData.toString() )
                .append( ",'version':" ).append( versionPacketData.toString() )
                .append( "}" )
                .toString();
    }
}
