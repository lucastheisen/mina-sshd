package org.apache.sshd.sftp.client.packetdata.impl;


import org.apache.sshd.sftp.PacketType;
import org.apache.sshd.sftp.client.packetdata.Attrs;
import org.apache.sshd.sftp.client.packetdata.Stat;


public class DefaultPacketDataStat
        extends AbstractPath<Stat>
        implements Stat {
    protected DefaultPacketDataStat() {
    }

    @Override
    public Class<Attrs> expectedResponseType() {
        return Attrs.class;
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.SSH_FXP_STAT;
    }
}
