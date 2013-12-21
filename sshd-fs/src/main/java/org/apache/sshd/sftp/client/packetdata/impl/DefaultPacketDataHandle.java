package org.apache.sshd.sftp.client.packetdata.impl;


import org.apache.sshd.sftp.PacketType;
import org.apache.sshd.sftp.client.packetdata.Handle;


public class DefaultPacketDataHandle
        extends AbstractHandle<Handle>
        implements Handle {
    protected DefaultPacketDataHandle() {}
    
    @Override
    public PacketType getPacketType() {
        return PacketType.SSH_FXP_HANDLE;
    }
}