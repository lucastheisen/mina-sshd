package org.apache.sshd.sftp;


public interface PacketDataFactory {
    public <T extends PacketData<T>> T newInstance( byte packetDataType );

    public <T extends PacketData<T>> T newInstance( Class<T> interfaceType );
}
