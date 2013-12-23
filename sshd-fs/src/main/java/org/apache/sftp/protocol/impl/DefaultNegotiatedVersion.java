package org.apache.sftp.protocol.impl;


import java.util.Map;


import org.apache.sftp.protocol.NegotiatedVersion;
import org.apache.sftp.protocol.packetdata.Init;
import org.apache.sftp.protocol.packetdata.Version;


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
