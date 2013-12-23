package org.apache.sftp.protocol.packetdata.impl;


import org.apache.sftp.protocol.PacketType;
import org.apache.sftp.protocol.packetdata.Name;
import org.apache.sftp.protocol.packetdata.ReadDir;


public class DefaultReadDir
        extends AbstractHandle<ReadDir>
        implements ReadDir {
    protected DefaultReadDir() {
    }

    @Override
    public Class<Name> expectedResponseType() {
        return Name.class;
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.SSH_FXP_READDIR;
    }
}