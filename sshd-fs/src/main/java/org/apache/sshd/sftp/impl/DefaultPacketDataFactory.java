package org.apache.sshd.sftp.impl;


import org.apache.sshd.sftp.PacketData;
import org.apache.sshd.sftp.PacketDataFactory;
import org.apache.sshd.sftp.PacketType;
import org.apache.sshd.sftp.client.packetdata.Close;
import org.apache.sshd.sftp.client.packetdata.Handle;
import org.apache.sshd.sftp.client.packetdata.Init;
import org.apache.sshd.sftp.client.packetdata.Name;
import org.apache.sshd.sftp.client.packetdata.OpenDir;
import org.apache.sshd.sftp.client.packetdata.ReadDir;
import org.apache.sshd.sftp.client.packetdata.Status;
import org.apache.sshd.sftp.client.packetdata.Version;


public class DefaultPacketDataFactory implements PacketDataFactory {
    @Override
    @SuppressWarnings("unchecked")
    public <T extends PacketData<T>> T newInstance( byte packetTypeByte ) {
        PacketType packetType = PacketType.fromValue( packetTypeByte );
        if ( packetType == null ) {
            return (T)new DefaultPacketDataRaw( packetTypeByte );
        }

        try {
            return newInstance( (Class<T>)packetType.getInterface() );
        }
        catch ( UnsupportedOperationException e ) {
            return (T)new DefaultPacketDataRaw( packetTypeByte );
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends PacketData<T>> T newInstance( Class<T> interfaceType ) {
        if ( Init.class.equals( interfaceType ) ) {
            return (T)newInit();
        }
        else if ( Version.class.equals( interfaceType ) ) {
            return (T)newVersion();
        }
        else if ( Handle.class.equals( interfaceType ) ) {
            return (T)newHandle();
        }
        else if ( OpenDir.class.equals( interfaceType ) ) {
            return (T)newOpenDir();
        }
        else if ( Status.class.equals( interfaceType ) ) {
            return (T)newStatus();
        }
        else if ( Close.class.equals( interfaceType ) ) {
            return (T)newClose();
        }
        else if ( ReadDir.class.equals( interfaceType ) ) {
            return (T)newReadDir();
        }
        else if ( Name.class.equals( interfaceType ) ) {
            return (T)newName();
        }
        throw new UnsupportedOperationException( interfaceType.getName() + " not yet implemented" );
    }

    public Close newClose() {
        return new DefaultPacketDataClose();
    }

    public Handle newHandle() {
        return new DefaultPacketDataHandle();
    }

    public Init newInit() {
        return new DefaultPacketDataInit();
    }

    public Name newName() {
        return new DefaultPacketDataName();
    }

    public OpenDir newOpenDir() {
        return new DefaultPacketDataOpenDir();
    }

    public ReadDir newReadDir() {
        return new DefaultPacketDataReadDir();
    }

    public Status newStatus() {
        return new DefaultPacketDataStatus();
    }

    public Version newVersion() {
        return new DefaultPacketDataVersion();
    }
}
