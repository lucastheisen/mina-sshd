package org.apache.sftp.protocol.packetdata.impl;


import org.apache.sftp.protocol.PacketType;
import org.apache.sftp.protocol.packetdata.MkDir;
import org.apache.sftp.protocol.packetdata.Status;


public class DefaultMkDir
        extends AbstractPathAttrs<MkDir>
        implements MkDir {
    @Override
    public Class<Status> expectedResponseType() {
        return Status.class;
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.SSH_FXP_MKDIR;
    }
}
