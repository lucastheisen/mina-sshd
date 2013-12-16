package org.apache.sshd.sftp.impl;


import org.apache.sshd.sftp.PacketType;
import org.apache.sshd.sftp.client.packetdata.Close;


public class DefaultPacketDataClose
        extends AbstractRequestOrResponse<Close>
        implements Close {
    private String handle;

    @Override
    public void appendToStringBuilder( StringBuilder builder ) {
        builder.append( "'handle':'" ).append( handle ).append( "'" );
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.SSH_FXP_CLOSE;
    }

    @Override
    public String getHandle() {
        return handle;
    }

    @Override
    public DefaultPacketDataClose parseRequestFrom( SftpProtocolBuffer buffer ) {
        handle = buffer.getString();
        return this;
    }

    @Override
    public DefaultPacketDataClose setHandle( String handle ) {
        this.handle = handle;
        return this;
    }

    @Override
    public void writeRequestTo( SftpProtocolBuffer buffer ) {
        buffer.putString( handle );
    }

}
