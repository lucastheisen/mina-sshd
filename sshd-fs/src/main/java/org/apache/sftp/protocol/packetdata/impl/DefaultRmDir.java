package org.apache.sftp.protocol.packetdata.impl;

import org.apache.sftp.protocol.PacketType;
import org.apache.sftp.protocol.packetdata.RmDir;
import org.apache.sftp.protocol.packetdata.Status;

public class DefaultRmDir extends AbstractPath<RmDir> implements RmDir {
    @Override
    public Class<Status> expectedResponseType() {
        return Status.class;
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.SSH_FXP_RMDIR;
    }
}
