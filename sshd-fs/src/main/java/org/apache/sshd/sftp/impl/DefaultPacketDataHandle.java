package org.apache.sshd.sftp.impl;


import org.apache.sshd.sftp.PacketType;
import org.apache.sshd.sftp.client.packetdata.Handle;


public class DefaultPacketDataHandle
        extends AbstractRequestOrResponse<Handle>
        implements Handle {
    private String handle;
    
    @Override
    public void appendToStringBuilder( StringBuilder builder ) {
        builder.append( "'handle':'" ).append( handle ).append( "'" );
    }

    @Override
    public String getHandle() {
        return handle;
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.SSH_FXP_HANDLE;
    }

    @Override
    public DefaultPacketDataHandle parseRequestFrom( SftpProtocolBuffer buffer ) {
        this.handle = buffer.getString();
        return this;
    }

    @Override
    public DefaultPacketDataHandle setHandle( String handle ) {
        this.handle = handle;
        return this;
    }

    @Override
    public void writeRequestTo( SftpProtocolBuffer buffer ) {
        buffer.putString( handle );
    }
}
