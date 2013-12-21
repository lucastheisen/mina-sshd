package org.apache.sshd.sftp.client.packetdata.impl;

import org.apache.sshd.sftp.PacketType;
import org.apache.sshd.sftp.client.packetdata.Data;
import org.apache.sshd.sftp.client.packetdata.Read;
import org.apache.sshd.sftp.impl.SftpProtocolBuffer;

public class DefaultPacketDataRead extends AbstractHandleOffset<Read> implements Read {
    private int length;
    
    @Override
    protected void appendToStringBuilder3( StringBuilder builder ) {
        builder.append( ",'length':" ).append( length );
    }

    @Override
    public Class<Data> expectedResponseType() {
        return Data.class;
    }

    @Override
    public int getLength() {
        return length;
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.SSH_FXP_READ;
    }
    
    @Override
    public Read parseFrom3( SftpProtocolBuffer buffer ) {
        length = buffer.getInt();
        return this;
    }

    @Override
    public Read setLength( int length ) {
        this.length = length;
        return this;
    }

    @Override
    public void writeTo3( SftpProtocolBuffer buffer ) {
        buffer.putInt( length );
    }
}
