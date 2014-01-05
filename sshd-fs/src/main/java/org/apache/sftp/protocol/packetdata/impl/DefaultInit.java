package org.apache.sftp.protocol.packetdata.impl;


import org.apache.sftp.protocol.PacketType;
import org.apache.sftp.protocol.packetdata.Init;


public class DefaultInit extends AbstractVersion<Init> implements Init {
    @Override
    public PacketType getPacketType() {
        return PacketType.SSH_FXP_INIT;
    }
}