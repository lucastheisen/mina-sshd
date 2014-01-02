package org.apache.sftp.protocol;


import org.apache.sftp.protocol.packetdata.Extended;


public interface PacketDataFactory {
    public <T extends Extended<T, S>, S extends Response<S>> T newExtended( String extendedRequest );

    public <T extends PacketData<T>> T newPacketData( PacketType packetType );

    public <T extends PacketData<T>> T newPacketData( Class<T> interfaceType );
}
