package org.apache.sftp.protocol.packetdata.openssh.impl;


import org.apache.sftp.protocol.PacketType;
import org.apache.sftp.protocol.impl.SftpProtocolBuffer;
import org.apache.sftp.protocol.packetdata.Status;
import org.apache.sftp.protocol.packetdata.openssh.PosixRename;


public class DefaultPosixRename extends AbstractExtendedPath<PosixRename, Status> implements PosixRename {
    private String targetPath;

    @Override
    public Class<Status> expectedResponseType() {
        return Status.class;
    }

    @Override
    public String getName() {
        return "posix-rename@openssh.com";
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.SSH_FXP_EXTENDED;
    }

    @Override
    public String getTargetPath() {
        return targetPath;
    }

    @Override
    public PosixRename parseFrom2( SftpProtocolBuffer buffer ) {
        targetPath = buffer.getString();
        return this;
    }

    @Override
    public PosixRename setTargetPath( String targetPath ) {
        this.targetPath = targetPath;
        return this;
    }

    @Override
    public void writeTo2( SftpProtocolBuffer buffer ) {
        buffer.putString( targetPath );
    }
}
