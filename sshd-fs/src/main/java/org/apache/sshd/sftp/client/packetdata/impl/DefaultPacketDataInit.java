package org.apache.sshd.sftp.client.packetdata.impl;


import org.apache.sshd.sftp.PacketType;
import org.apache.sshd.sftp.client.packetdata.Init;


public class DefaultPacketDataInit extends AbstractVersion<Init> implements Init {
    protected DefaultPacketDataInit() {}
    
    @Override
    public PacketType getPacketType() {
        return PacketType.SSH_FXP_INIT;
    }
}