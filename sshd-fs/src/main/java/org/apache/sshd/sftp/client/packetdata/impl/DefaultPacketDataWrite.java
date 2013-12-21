package org.apache.sshd.sftp.client.packetdata.impl;


import org.apache.sshd.sftp.PacketType;
import org.apache.sshd.sftp.client.packetdata.Status;
import org.apache.sshd.sftp.client.packetdata.Write;
import org.apache.sshd.sftp.impl.SftpProtocolBuffer;


public class DefaultPacketDataWrite extends AbstractHandleOffset<Write> implements Write {
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
