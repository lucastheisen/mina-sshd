package org.apache.sshd.sftp.impl;


import org.apache.sshd.sftp.PacketData;
import org.apache.sshd.sftp.PacketDataFactory;
import org.apache.sshd.sftp.PacketType;


public class DefaultPacketDataFactory implements PacketDataFactory {
    @Override
    public PacketData newInstance( PacketType packetType ) {
        switch ( packetType ) {
            case SSH_FXP_INIT:
                return new DefaultInit();
            case SSH_FXP_VERSION:
                return new DefaultVersion();
            default:
                throw new UnsupportedOperationException( packetType + " not yet supported" );
        }
    }
}
