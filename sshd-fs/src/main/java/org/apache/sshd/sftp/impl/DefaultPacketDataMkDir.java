package org.apache.sshd.sftp.impl;


import org.apache.sshd.sftp.PacketType;
import org.apache.sshd.sftp.client.packetdata.MkDir;


public class DefaultPacketDataMkDir
        extends AbstractRequestOrResponse<MkDir>
        implements MkDir {
    private SftpFileAttributes fileAttributes;
    private String path;

    @Override
    public void appendToStringBuilder( StringBuilder builder ) {
        builder.append( ",'path':'" ).append( path )
                .append( "','fileAttributes':" ).append( fileAttributes.toString() );
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.SSH_FXP_MKDIR;
    }

    @Override
    public SftpFileAttributes getFileAttributes() {
        return fileAttributes;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public DefaultPacketDataMkDir parseRequestFrom( SftpProtocolBuffer buffer ) {
        path = buffer.getString();
        fileAttributes = new SftpFileAttributes().parseFrom( buffer );
        return this;
    }

    @Override
    public DefaultPacketDataMkDir setFileAttributes( SftpFileAttributes fileAttributes ) {
        this.fileAttributes = fileAttributes;
        return this;
    }

    @Override
    public DefaultPacketDataMkDir setPath( String path ) {
        this.path = path;
        return this;
    }

    @Override
    public void writeRequestTo( SftpProtocolBuffer buffer ) {
        buffer.putString( path );
        fileAttributes.writeTo( buffer );
    }
}
