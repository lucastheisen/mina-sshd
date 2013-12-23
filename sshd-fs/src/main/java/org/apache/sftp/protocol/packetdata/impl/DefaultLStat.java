package org.apache.sftp.protocol.packetdata.impl;


import org.apache.sftp.protocol.PacketType;
import org.apache.sftp.protocol.packetdata.Attrs;
import org.apache.sftp.protocol.packetdata.LStat;


public class DefaultLStat extends AbstractPath<LStat> implements LStat {
    @Override
    public Class<Attrs> expectedResponseType() {
        return Attrs.class;
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.SSH_FXP_LSTAT;
    }
}
