package org.apache.sftp.protocol.packetdata.impl;


import org.apache.sftp.protocol.PacketType;
import org.apache.sftp.protocol.packetdata.Attrs;
import org.apache.sftp.protocol.packetdata.FStat;


public class DefaultFStat extends AbstractHandle<FStat> implements FStat {
    @Override
    public Class<Attrs> expectedResponseType() {
        return Attrs.class;
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.SSH_FXP_FSTAT;
    }
}
