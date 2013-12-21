package org.apache.sshd.sftp.client.packetdata.impl;


import org.apache.sshd.sftp.PacketType;
import org.apache.sshd.sftp.client.packetdata.SetStat;
import org.apache.sshd.sftp.client.packetdata.Status;
import org.apache.sshd.sftp.impl.SftpFileAttributes;
import org.apache.sshd.sftp.impl.SftpProtocolBuffer;


public class DefaultPacketDataSetStat
        extends AbstractPath<SetStat>
        implements SetStat {
    private SftpFileAttributes fileAttributes;

    protected DefaultPacketDataSetStat() {
    }

    @Override
    public Class<Status> expectedResponseType() {
        return Status.class;
    }

    protected void appendToStringBuilder2( StringBuilder builder ) {
        if ( fileAttributes != null ) {
            builder.append( ",'fileAttributes':" ).append( fileAttributes.toString() );
        }
    }

    @Override
    public SftpFileAttributes getFileAttributes() {
        return fileAttributes;
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.SSH_FXP_SETSTAT;
    }

    protected SetStat parseFrom2( SftpProtocolBuffer buffer ) {
        fileAttributes = new SftpFileAttributes().parseFrom( buffer );
        return this;
    }

    @Override
    public SetStat setFileAttributes( SftpFileAttributes fileAttributes ) {
        this.fileAttributes = fileAttributes;
        return this;
    }

    @Override
    protected void writeTo2( SftpProtocolBuffer buffer ) {
        fileAttributes.writeTo( buffer );
    }
}
