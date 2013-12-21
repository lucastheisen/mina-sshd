package org.apache.sshd.sftp.client.packetdata.impl;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


import org.apache.sshd.sftp.PacketData;
import org.apache.sshd.sftp.PacketType;
import org.apache.sshd.sftp.impl.SftpProtocolBuffer;


public abstract class AbstractVersion<T> implements PacketData<T> {
    private Map<String, String> extensions;
    private int version;

    public AbstractVersion() {
        extensions = new HashMap<>();
    }

    @SuppressWarnings("unchecked")
    public T addExtension( String name, String data ) {
        extensions.put( name, data );
        data = null;
        return (T)this;
    }

    abstract public PacketType getPacketType();

    @Override
    public byte getPacketTypeByte() {
        return getPacketType().getValue();
    }

    public Map<String, String> getExtensions() {
        return Collections.unmodifiableMap( extensions );
    }
    
    public int getVersion() {
        return version;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T parseFrom( SftpProtocolBuffer buffer ) {
        version = buffer.getInt();
        if ( buffer.hasRemaining() ) {
            while ( buffer.hasRemaining() ) {
                extensions.put( buffer.getString(), buffer.getString() );
            }
        }
        return (T)this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T setPacketTypeByte( byte packetDataTypeByte ) {
        if ( getPacketType() != PacketType.fromValue( packetDataTypeByte ) ) {
            throw new UnsupportedOperationException( "trying to change packet type of a concrete implementation of packet data is illegal" );
        }
        return (T)this;
    }

    @SuppressWarnings("unchecked")
    public T setVersion( int version ) {
        this.version = version;
        return (T)this;
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
