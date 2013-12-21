package org.apache.sshd.sftp.client.packetdata.impl;


import org.apache.sshd.sftp.PacketType;
import org.apache.sshd.sftp.client.packetdata.Version;


public class DefaultPacketDataVersion extends AbstractVersion<Version> implements Version {
    protected DefaultPacketDataVersion() {
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.SSH_FXP_INIT;
    }
}
