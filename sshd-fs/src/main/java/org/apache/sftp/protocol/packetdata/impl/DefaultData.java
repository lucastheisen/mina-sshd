package org.apache.sftp.protocol.packetdata.impl;

import org.apache.sftp.protocol.PacketType;
import org.apache.sftp.protocol.impl.SftpProtocolBuffer;
import org.apache.sftp.protocol.packetdata.Data;

public class DefaultData extends AbstractPacketData<Data> implements Data {
    private byte[] data;

    @Override
    public void appendToStringBuilder( StringBuilder builder ) {
        builder.append( "'data':{'size':" ).append( data.length ).append( "}" );
    }

    @Override
    public byte[] getData() {
        return data;
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.SSH_FXP_DATA;
    }

    @Override
    public Data parseFrom( SftpProtocolBuffer buffer ) {
        data = buffer.getByteString();
        return this;
    }

    @Override
    public Data setData( byte[] data ) {
        this.data = data;
        return this;
    }

    @Override
    public void writeTo( SftpProtocolBuffer buffer ) {
        buffer.putByteString( data );
    }
}
