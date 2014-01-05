package org.apache.sftp.protocol.packetdata.impl;


import org.apache.sftp.protocol.PacketType;
import org.apache.sftp.protocol.packetdata.Handle;


public class DefaultHandle
        extends AbstractHandle<Handle>
        implements Handle {
    @Override
    public PacketType getPacketType() {
        return PacketType.SSH_FXP_HANDLE;
    }
}