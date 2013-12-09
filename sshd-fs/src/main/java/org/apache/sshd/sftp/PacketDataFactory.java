package org.apache.sshd.sftp;


public interface PacketDataFactory {
    public PacketData newInstance( PacketType packetType );
}
