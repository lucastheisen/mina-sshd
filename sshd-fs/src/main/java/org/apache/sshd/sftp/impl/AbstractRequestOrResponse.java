package org.apache.sshd.sftp.impl;


import org.apache.sshd.sftp.PacketType;
import org.apache.sshd.sftp.RequestOrResponse;


public abstract class AbstractRequestOrResponse<T> implements RequestOrResponse<T> {
    int id;

    abstract public void appendToStringBuilder( StringBuilder builder );

    @Override
    public int getId() {
        return id;
    }

    @Override
    public byte getPacketTypeByte() {
        return getPacketType().getValue();
    }

    @Override
    public T parseFrom( SftpProtocolBuffer buffer ) {
        id = buffer.getInt();
        return parseRequestFrom( buffer );
    }

    abstract public T parseRequestFrom( SftpProtocolBuffer buffer );

    @SuppressWarnings("unchecked")
    public T setId( int id ) {
        this.id = id;
        return (T)this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T setPacketTypeByte( byte packetDataTypeByte ) {
        if ( getPacketType() != PacketType.fromValue( packetDataTypeByte ) ) {
            throw new UnsupportedOperationException( "trying to change packet type of a concrete implementation of packet data is illegal" );
        }
        return (T)this;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder( "{'packetType':" );
        PacketType packetType = getPacketType();
        if ( packetType == null ) {
            builder.append( getPacketTypeByte() );
        }
        else {
            builder.append( "'" ).append( packetType ).append( "'" );
        }
        builder.append( ",'id':" ).append( id );
        appendToStringBuilder( builder );
        return builder.append( "}" ).toString();
    }

    abstract public void writeRequestTo( SftpProtocolBuffer buffer );

    @Override
    public void writeTo( SftpProtocolBuffer buffer ) {
        buffer.putInt( id );
        writeRequestTo( buffer );
    }
}
