package org.apache.sftp.protocol;


public interface PacketDataFactory {
    public <T extends PacketData<T>> T newInstance( byte packetDataType );

    public <T extends PacketData<T>> T newInstance( Class<T> interfaceType );
}
