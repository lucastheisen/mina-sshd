package org.apache.sshd.sftp.impl;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


import org.apache.sshd.sftp.PacketType;
import org.apache.sshd.sftp.client.packetdata.Version;


public class DefaultPacketDataVersion implements Version {
    private Map<String, String> extensions;
    private int version;

    public DefaultPacketDataVersion() {
        extensions = new HashMap<>();
    }

    @Override
    public DefaultPacketDataVersion addExtension( String name, String data ) {
        extensions.put( name, data );
        data = null;
        return this;
    }

    @Override
    public Map<String, String> getExtensions() {
        return Collections.unmodifiableMap( extensions );
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.SSH_FXP_INIT;
    }

    @Override
    public byte getPacketTypeByte() {
        return getPacketType().getValue();
    }

    @Override
    public int getVersion() {
        return version;
    }

    @Override
    public DefaultPacketDataVersion parseFrom( SftpProtocolBuffer buffer ) {
        version = buffer.getInt();
        if ( buffer.hasRemaining() ) {
            while ( buffer.hasRemaining() ) {
                extensions.put( buffer.getString(), buffer.getString() );
            }
        }
        return this;
    }

    @Override
    public DefaultPacketDataVersion setPacketTypeByte( byte packetDataTypeByte ) {
        if ( getPacketType() != PacketType.fromValue( packetDataTypeByte ) ) {
            throw new UnsupportedOperationException( "trying to change packet type of a concrete implementation of packet data is illegal" );
        }
        return this;
    }

    @Override
    public DefaultPacketDataVersion setVersion( int version ) {
        this.version = version;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder( "{'version':'" )
                .append( version )
                .append( "'" );
        if ( !extensions.isEmpty() ) {
            builder.append( ",'extensions':{" );
            boolean first = true;
            for ( Map.Entry<String, String> extension : extensions.entrySet() ) {
                if ( first ) {
                    first = false;
                }
                else {
                    builder.append( "," );
                }
                builder.append( "'" )
                        .append( extension.getKey() )
                        .append( "':'" )
                        .append( extension.getValue() )
                        .append( "'" );
            }
            builder.append( "}" );
        }
        return builder.append( "}" ).toString();
    }

    @Override
    public void writeTo( SftpProtocolBuffer buffer ) {
        buffer.putInt( version );
        for ( Map.Entry<String, String> extension : extensions.entrySet() ) {
            buffer.putString( extension.getKey() );
            buffer.putString( extension.getValue() );
        }
    }
}
