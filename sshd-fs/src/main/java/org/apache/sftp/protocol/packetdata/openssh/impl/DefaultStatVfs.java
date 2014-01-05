package org.apache.sftp.protocol.packetdata.openssh.impl;


import org.apache.sftp.protocol.PacketType;
import org.apache.sftp.protocol.packetdata.openssh.StatVfs;
import org.apache.sftp.protocol.packetdata.openssh.StatVfsReply;


public class DefaultStatVfs extends AbstractExtendedPath<StatVfs, StatVfsReply> implements StatVfs {
    @Override
    public Class<StatVfsReply> expectedResponseType() {
        return StatVfsReply.class;
    }

    @Override
    public String getExtendedRequest() {
        return "statvfs@openssh.com";
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.SSH_FXP_EXTENDED;
    }
}
