package org.apache.sshd.sftp.impl;


import org.apache.sshd.sftp.PacketType;
import org.apache.sshd.sftp.client.packetdata.Raw;


public class DefaultPacketDataRaw
        extends AbstractRequestOrResponse<Raw>
        implements Raw {
    private byte packetTypeByte;
    private byte[] bytes;

    public DefaultPacketDataRaw( byte packetTypeByte ) {
        this.packetTypeByte = packetTypeByte;
    }
    
    @Override
    public void appendToStringBuilder( StringBuilder builder ) {
        builder.append( ",'bytes':[" ).append( bytes ).append( "]" );
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
    public byte[] getBytes() {
        return bytes;
    }

    @Override
    public DefaultPacketDataRaw parseRequestFrom( SftpProtocolBuffer buffer ) {
        bytes = new byte[buffer.remaining()];
        buffer.get( bytes );
        return this;
    }

    @Override
    public DefaultPacketDataRaw setBytes( byte[] bytes ) {
        this.bytes = bytes;
        return this;
    }

    @Override
    public void writeRequestTo( SftpProtocolBuffer buffer ) {
        buffer.put( bytes );
    }
}
