package org.apache.sftp.protocol.packetdata.openssh.impl;


import org.apache.sftp.protocol.PacketType;
import org.apache.sftp.protocol.packetdata.openssh.FStatVfs;
import org.apache.sftp.protocol.packetdata.openssh.StatVfsReply;


public class DefaultFStatVfs extends AbstractExtendedHandle<FStatVfs, StatVfsReply> implements FStatVfs {
    @Override
    public Class<StatVfsReply> expectedResponseType() {
        return StatVfsReply.class;
    }

    @Override
    public String getExtendedRequest() {
        return "fstatvfs@openssh.com";
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.SSH_FXP_EXTENDED;
    }
}
