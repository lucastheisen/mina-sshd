package org.apache.sshd.sftp.impl;


import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


import org.apache.sshd.sftp.PacketData;
import org.apache.sshd.sftp.PacketType;
import org.apache.sshd.sftp.client.packetdata.Version;
import org.apache.sshd.sftp.impl.SftpProtocolBuffer.PackBuffer;
import org.apache.sshd.sftp.impl.SftpProtocolBuffer.PackCallback;


public class DefaultVersion implements PacketData, Version {
    private SftpProtocolBuffer data;
    private Map<String, String> extensions;
    private int version;

    public DefaultVersion() {
        extensions = new HashMap<>();
    }

    @Override
    public DefaultVersion addExtension( String name, String data ) {
        extensions.put( name, data );
        data = null;
        return this;
    }

    private SftpProtocolBuffer getData() {
        if ( data == null ) {
            data = SftpProtocolBuffer.pack( new PackCallback() {
                @Override
                public void pack( PackBuffer packBuffer ) {
                    packBuffer.putInt( version );
                    for ( Map.Entry<String, String> extension : extensions.entrySet() ) {
                        packBuffer.putString( extension.getKey() );
                        packBuffer.putString( extension.getValue() );
                    }
                }
            } );
        }
        return data.asReadOnlyBuffer();
    }

    @Override
    public Map<String, String> getExtensions() {
        return Collections.unmodifiableMap( extensions );
    }

    public PacketType getPacketType() {
        return PacketType.SSH_FXP_INIT;
    }

    public int getSize() {
        return getData().remaining();
    }

    @Override
    public int getVersion() {
        return version;
    }

    @Override
    public DefaultVersion parseFrom( SftpProtocolBuffer buffer ) {
        this.data = buffer;
        version = data.getInt();
        if ( data.hasRemaining() ) {
            while ( data.hasRemaining() ) {
                extensions.put( data.getString(), data.getString() );
            }
        }
        return this;
    }

    @Override
    public DefaultVersion setVersion( int version ) {
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
    public void writeTo( SftpProtocolBuffer buffer ) throws IOException {
        buffer.put( getData() );
    }
}
