package org.apache.sftp.protocol.packetdata.impl;

import org.apache.sftp.protocol.PacketType;
import org.apache.sftp.protocol.packetdata.Name;
import org.apache.sftp.protocol.packetdata.ReadLink;

public class DefaultReadLink extends AbstractPath<ReadLink> implements ReadLink {
    @Override
    public Class<Name> expectedResponseType() {
        return Name.class;
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.SSH_FXP_READLINK;
    }
}
