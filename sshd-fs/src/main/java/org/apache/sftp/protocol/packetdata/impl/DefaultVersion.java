package org.apache.sftp.protocol.packetdata.impl;


import org.apache.sftp.protocol.PacketType;
import org.apache.sftp.protocol.packetdata.Version;


public class DefaultVersion extends AbstractVersion<Version> implements Version {
    protected DefaultVersion() {
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.SSH_FXP_INIT;
    }
}
