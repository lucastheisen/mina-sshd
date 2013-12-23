package org.apache.sftp.protocol.packetdata.impl;


import org.apache.sftp.protocol.PacketType;
import org.apache.sftp.protocol.packetdata.Rename;
import org.apache.sftp.protocol.packetdata.Status;


public class DefaultRename extends AbstractPathTargetPath<Rename> implements Rename {
    @Override
    public Class<Status> expectedResponseType() {
        return Status.class;
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.SSH_FXP_RENAME;
    }
}
