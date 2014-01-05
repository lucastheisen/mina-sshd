package org.apache.sftp.protocol.packetdata.impl;


import org.apache.sftp.protocol.PacketType;
import org.apache.sftp.protocol.packetdata.Handle;
import org.apache.sftp.protocol.packetdata.OpenDir;


public class DefaultOpenDir
        extends AbstractPath<OpenDir>
        implements OpenDir {
    @Override
    public Class<Handle> expectedResponseType() {
        return Handle.class;
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.SSH_FXP_OPENDIR;
    }
}
