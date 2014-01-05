package org.apache.sftp.protocol.packetdata.openssh.impl;


import org.apache.sftp.protocol.PacketType;
import org.apache.sftp.protocol.packetdata.Status;
import org.apache.sftp.protocol.packetdata.openssh.FSync;


public class DefaultFSync extends AbstractExtendedHandle<FSync, Status> implements FSync {
    @Override
    public Class<Status> expectedResponseType() {
        return Status.class;
    }

    @Override
    public String getExtendedRequest() {
        return "fsync@openssh.com";
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.SSH_FXP_EXTENDED;
    }
}
