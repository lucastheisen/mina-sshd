package org.apache.sftp.protocol.packetdata.impl;

import org.apache.sftp.protocol.PacketType;
import org.apache.sftp.protocol.impl.SftpProtocolBuffer;
import org.apache.sftp.protocol.packetdata.Data;
import org.apache.sftp.protocol.packetdata.Read;

public class DefaultRead extends AbstractHandleOffset<Read> implements Read {
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
