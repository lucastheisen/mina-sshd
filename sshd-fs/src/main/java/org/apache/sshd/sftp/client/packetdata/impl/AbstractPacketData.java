package org.apache.sshd.sftp.client.packetdata.impl;


import org.apache.sshd.sftp.PacketData;
import org.apache.sshd.sftp.PacketType;


public abstract class AbstractPacketData<T> implements PacketData<T> {
    protected void appendToStringBuilder( StringBuilder builder ) {
    }

    @Override
    public byte getPacketTypeByte() {
        return getPacketType().getValue();
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
        appendToStringBuilder( builder );
        return builder.append( "}" ).toString();
    }
}
