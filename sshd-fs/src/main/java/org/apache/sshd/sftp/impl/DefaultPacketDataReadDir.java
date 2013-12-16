package org.apache.sshd.sftp.impl;


import org.apache.sshd.sftp.PacketType;
import org.apache.sshd.sftp.client.packetdata.ReadDir;


public class DefaultPacketDataReadDir
        extends AbstractRequestOrResponse<ReadDir>
        implements ReadDir {
    private String handle;

    @Override
    public void appendToStringBuilder( StringBuilder builder ) {
        builder.append( "'handle':'" ).append( handle ).append( "'" );
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.SSH_FXP_READDIR;
    }

    @Override
    public String getHandle() {
        return handle;
    }

    @Override
    public DefaultPacketDataReadDir parseRequestFrom( SftpProtocolBuffer buffer ) {
        handle = buffer.getString();
        return this;
    }

    @Override
    public DefaultPacketDataReadDir setHandle( String handle ) {
        this.handle = handle;
        return this;
    }

    @Override
    public void writeRequestTo( SftpProtocolBuffer buffer ) {
        buffer.putString( handle );
    }
}
