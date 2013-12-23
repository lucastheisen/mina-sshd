package org.apache.sftp.protocol.packetdata.impl;


import org.apache.sftp.protocol.PacketType;
import org.apache.sftp.protocol.packetdata.Close;
import org.apache.sftp.protocol.packetdata.Status;


public class DefaultClose
        extends AbstractHandle<Close>
        implements Close {
    protected DefaultClose() {}

    @Override
    public Class<Status> expectedResponseType() {
        return Status.class;
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.SSH_FXP_CLOSE;
    }
}
