package org.apache.sshd.sftp.client.packetdata.impl;


import org.apache.sshd.sftp.PacketType;
import org.apache.sshd.sftp.client.packetdata.MkDir;
import org.apache.sshd.sftp.client.packetdata.Status;
import org.apache.sshd.sftp.impl.SftpFileAttributes;
import org.apache.sshd.sftp.impl.SftpProtocolBuffer;


public class DefaultPacketDataMkDir
        extends AbstractPacketData<MkDir>
        implements MkDir {
    private SftpFileAttributes fileAttributes;
    private String path;

    protected DefaultPacketDataMkDir() {}

    @Override
    public void appendToStringBuilder( StringBuilder builder ) {
        builder.append( ",'path':'" ).append( path )
                .append( "','fileAttributes':" ).append( fileAttributes.toString() );
    }

    @Override
    public Class<Status> expectedResponseType() {
        return Status.class;
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
    public DefaultPacketDataMkDir parseFrom( SftpProtocolBuffer buffer ) {
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
    public void writeTo( SftpProtocolBuffer buffer ) {
        buffer.putString( path );
        fileAttributes.writeTo( buffer );
    }
}
