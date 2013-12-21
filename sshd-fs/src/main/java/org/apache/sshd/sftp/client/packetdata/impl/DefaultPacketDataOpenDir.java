package org.apache.sshd.sftp.client.packetdata.impl;


import org.apache.sshd.sftp.PacketType;
import org.apache.sshd.sftp.client.packetdata.Handle;
import org.apache.sshd.sftp.client.packetdata.OpenDir;


public class DefaultPacketDataOpenDir
        extends AbstractPath<OpenDir>
        implements OpenDir {

    protected DefaultPacketDataOpenDir() {
    }

    @Override
    public Class<Handle> expectedResponseType() {
        return Handle.class;
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.SSH_FXP_OPENDIR;
    }
}
