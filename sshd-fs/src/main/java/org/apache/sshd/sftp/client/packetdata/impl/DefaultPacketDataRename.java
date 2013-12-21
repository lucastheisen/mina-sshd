package org.apache.sshd.sftp.client.packetdata.impl;


import org.apache.sshd.sftp.PacketType;
import org.apache.sshd.sftp.client.packetdata.Rename;
import org.apache.sshd.sftp.client.packetdata.Status;


public class DefaultPacketDataRename extends AbstractPathTargetPath<Rename> implements Rename {
    @Override
    public Class<Status> expectedResponseType() {
        return Status.class;
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.SSH_FXP_RENAME;
    }
}
