package org.apache.sshd.sftp.impl;


import org.apache.sshd.sftp.PacketType;
import org.apache.sshd.sftp.client.packetdata.OpenDir;


public class DefaultPacketDataOpenDir
        extends AbstractRequestOrResponse<OpenDir>
        implements OpenDir {
    private String path;
    
    @Override
    public void appendToStringBuilder( StringBuilder builder ) {
        builder.append( ",'path':'" ).append( path ).append( "'" );
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.SSH_FXP_OPENDIR;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public DefaultPacketDataOpenDir parseRequestFrom( SftpProtocolBuffer buffer ) {
        path = buffer.getString();
        return this;
    }

    @Override
    public DefaultPacketDataOpenDir setPath( String path ) {
        this.path = path;
        return this;
    }

    @Override
    public void writeRequestTo( SftpProtocolBuffer buffer ) {
        buffer.putString( path );
    }
}
