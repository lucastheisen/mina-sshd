package org.apache.sftp.protocol.packetdata.impl;


import org.apache.sftp.protocol.PacketType;
import org.apache.sftp.protocol.impl.SftpProtocolBuffer;
import org.apache.sftp.protocol.packetdata.Raw;


public class DefaultRaw
        extends AbstractPacketData<Raw>
        implements Raw {
    private byte packetTypeByte;
    private byte[] data;

    protected DefaultRaw( byte packetTypeByte ) {
        this.packetTypeByte = packetTypeByte;
    }

    @Override
    public void appendToStringBuilder( StringBuilder builder ) {
        builder.append( ",'bytes':[" ).append( data ).append( "]" );
    }

    @Override
    public Class<Raw> expectedResponseType() {
        return Raw.class;
    }

    @Override
    public PacketType getPacketType() {
        return null;
    }

    @Override
    public byte getPacketTypeByte() {
        return packetTypeByte;
    }

    @Override
    public byte[] getData() {
        return data;
    }

    @Override
    public DefaultRaw parseFrom( SftpProtocolBuffer buffer ) {
        data = new byte[buffer.remaining()];
        buffer.get( data );
        return this;
    }

    @Override
    public DefaultRaw setData( byte[] data ) {
        this.data = data;
        return this;
    }

    @Override
    public void writeTo( SftpProtocolBuffer buffer ) {
        buffer.put( data );
    }
}
