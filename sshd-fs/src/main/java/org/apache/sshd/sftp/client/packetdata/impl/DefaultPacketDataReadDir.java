package org.apache.sshd.sftp.client.packetdata.impl;


import org.apache.sshd.sftp.PacketType;
import org.apache.sshd.sftp.client.packetdata.Name;
import org.apache.sshd.sftp.client.packetdata.ReadDir;


public class DefaultPacketDataReadDir
        extends AbstractHandle<ReadDir>
        implements ReadDir {
    protected DefaultPacketDataReadDir() {
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