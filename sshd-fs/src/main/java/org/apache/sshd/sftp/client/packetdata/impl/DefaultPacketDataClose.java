package org.apache.sshd.sftp.client.packetdata.impl;


import org.apache.sshd.sftp.PacketType;
import org.apache.sshd.sftp.client.packetdata.Close;
import org.apache.sshd.sftp.client.packetdata.Status;


public class DefaultPacketDataClose
        extends AbstractHandle<Close>
        implements Close {
    protected DefaultPacketDataClose() {}

    @Override
    public Class<Status> expectedResponseType() {
        return Status.class;
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.SSH_FXP_CLOSE;
    }
}
