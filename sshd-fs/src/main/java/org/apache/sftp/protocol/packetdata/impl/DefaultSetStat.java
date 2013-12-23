package org.apache.sftp.protocol.packetdata.impl;


import org.apache.sftp.protocol.PacketType;
import org.apache.sftp.protocol.packetdata.SetStat;
import org.apache.sftp.protocol.packetdata.Status;


public class DefaultSetStat extends AbstractPathAttrs<SetStat> implements SetStat {
    @Override
    public Class<Status> expectedResponseType() {
        return Status.class;
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.SSH_FXP_SETSTAT;
    }
}
