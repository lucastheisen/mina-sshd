package org.apache.sftp.protocol.packetdata.impl;


import org.apache.sftp.protocol.PacketType;
import org.apache.sftp.protocol.impl.SftpFileAttributes;
import org.apache.sftp.protocol.impl.SftpProtocolBuffer;
import org.apache.sftp.protocol.packetdata.FSetStat;
import org.apache.sftp.protocol.packetdata.Status;


public class DefaultFSetStat extends AbstractHandle<FSetStat> implements FSetStat {
    private SftpFileAttributes fileAttributes;

    @Override
    protected void appendToStringBuilder2( StringBuilder builder ) {
        builder.append( ",'fileAttributes':" ).append( fileAttributes );
    }

    @Override
    public Class<Status> expectedResponseType() {
        return Status.class;
    }

    @Override
    public SftpFileAttributes getFileAttributes() {
        return fileAttributes;
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.SSH_FXP_FSETSTAT;
    }

    @Override
    protected FSetStat parseFrom2( SftpProtocolBuffer buffer ) {
        fileAttributes = new SftpFileAttributes().parseFrom( buffer );
        return this;
    }

    @Override
    public FSetStat setFileAttributes( SftpFileAttributes fileAttributes ) {
        this.fileAttributes = fileAttributes == null
                ? new SftpFileAttributes()
                : fileAttributes;
        return this;
    }

    @Override
    protected void writeTo2( SftpProtocolBuffer buffer ) {
        fileAttributes.writeTo( buffer );
    }
}
