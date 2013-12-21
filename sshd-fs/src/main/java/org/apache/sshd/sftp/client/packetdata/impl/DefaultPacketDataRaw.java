package org.apache.sshd.sftp.client.packetdata.impl;


import org.apache.sshd.sftp.PacketType;
import org.apache.sshd.sftp.client.packetdata.Raw;
import org.apache.sshd.sftp.impl.SftpProtocolBuffer;


public class DefaultPacketDataRaw
        extends AbstractPacketData<Raw>
        implements Raw {
    private byte packetTypeByte;
    private byte[] data;

    protected DefaultPacketDataRaw( byte packetTypeByte ) {
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
    public DefaultPacketDataRaw parseFrom( SftpProtocolBuffer buffer ) {
        data = new byte[buffer.remaining()];
        buffer.get( data );
        return this;
    }

    @Override
    public DefaultPacketDataRaw setData( byte[] data ) {
        this.data = data;
        return this;
    }

    @Override
    public void writeTo( SftpProtocolBuffer buffer ) {
        buffer.put( data );
    }
}
