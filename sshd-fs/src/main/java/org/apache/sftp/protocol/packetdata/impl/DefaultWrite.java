package org.apache.sftp.protocol.packetdata.impl;


import org.apache.sftp.protocol.PacketType;
import org.apache.sftp.protocol.impl.SftpProtocolBuffer;
import org.apache.sftp.protocol.packetdata.Status;
import org.apache.sftp.protocol.packetdata.Write;


public class DefaultWrite extends AbstractHandleOffset<Write> implements Write {
    private byte[] data;

    @Override
    protected void appendToStringBuilder3( StringBuilder builder ) {
        if ( data != null ) {
            builder.append( ",'data':{'size':" ).append( data.length ).append( "}" );
        }
    }

    @Override
    public Class<Status> expectedResponseType() {
        return Status.class;
    }

    @Override
    public byte[] getData() {
        return data;
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.SSH_FXP_WRITE;
    }

    @Override
    public Write parseFrom3( SftpProtocolBuffer buffer ) {
        data = buffer.getByteString();
        return this;
    }

    @Override
    public Write setData( byte[] data ) {
        this.data = data;
        return this;
    }

    @Override
    public void writeTo3( SftpProtocolBuffer buffer ) {
        buffer.putByteString( data );
    }
}
