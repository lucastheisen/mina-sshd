package org.apache.sftp.protocol.packetdata.impl;


import org.apache.sftp.protocol.PacketType;
import org.apache.sftp.protocol.packetdata.Status;
import org.apache.sftp.protocol.packetdata.SymLink;


public class DefaultSymLink extends AbstractPathTargetPath<SymLink> implements SymLink {
    @Override
    public Class<Status> expectedResponseType() {
        return Status.class;
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.SSH_FXP_SYMLINK;
    }
}
