package org.apache.sftp.protocol.packetdata.impl;

import org.apache.sftp.protocol.PacketType;
import org.apache.sftp.protocol.packetdata.Name;
import org.apache.sftp.protocol.packetdata.RealPath;

public class DefaultRealPath extends AbstractPath<RealPath> implements RealPath {
    @Override
    public Class<Name> expectedResponseType() {
        return Name.class;
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.SSH_FXP_REALPATH;
    }
}
