package org.apache.sftp.protocol.packetdata.impl;

import org.apache.sftp.protocol.PacketType;
import org.apache.sftp.protocol.packetdata.Remove;
import org.apache.sftp.protocol.packetdata.Status;

public class DefaultRemove extends AbstractPath<Remove> implements Remove {
    @Override
    public Class<Status> expectedResponseType() {
        return Status.class;
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.SSH_FXP_REMOVE;
    }
}
