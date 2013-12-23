package org.apache.sftp.protocol.packetdata.openssh.impl;


import org.apache.sftp.protocol.PacketData;
import org.apache.sftp.protocol.packetdata.impl.DefaultPacketDataFactory;
import org.apache.sftp.protocol.packetdata.openssh.PosixRename;


public class OpenSshPacketDataFactory extends DefaultPacketDataFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T extends PacketData<T>> T newExtended( Class<T> interfaceType ) {
        if ( PosixRename.class.equals( interfaceType ) ) {
            return (T)newPosixRename();
        }
        throw new UnsupportedOperationException( interfaceType.getName() + " not yet implemented" );
    }

    @Override
    public <T extends PacketData<T>> T newExtendedReply( Class<T> interfaceType ) {
        throw new UnsupportedOperationException( interfaceType.getName() + " not yet implemented" );
    }

    private PosixRename newPosixRename() {
        return new DefaultPosixRename();
    }
}
