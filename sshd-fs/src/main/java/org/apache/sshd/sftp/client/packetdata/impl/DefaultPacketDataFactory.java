package org.apache.sshd.sftp.client.packetdata.impl;


import org.apache.sshd.sftp.PacketData;
import org.apache.sshd.sftp.PacketDataFactory;
import org.apache.sshd.sftp.PacketType;
import org.apache.sshd.sftp.client.packetdata.Attrs;
import org.apache.sshd.sftp.client.packetdata.Close;
import org.apache.sshd.sftp.client.packetdata.Data;
import org.apache.sshd.sftp.client.packetdata.Handle;
import org.apache.sshd.sftp.client.packetdata.Init;
import org.apache.sshd.sftp.client.packetdata.Name;
import org.apache.sshd.sftp.client.packetdata.Open;
import org.apache.sshd.sftp.client.packetdata.OpenDir;
import org.apache.sshd.sftp.client.packetdata.Read;
import org.apache.sshd.sftp.client.packetdata.ReadDir;
import org.apache.sshd.sftp.client.packetdata.Rename;
import org.apache.sshd.sftp.client.packetdata.Stat;
import org.apache.sshd.sftp.client.packetdata.Status;
import org.apache.sshd.sftp.client.packetdata.SymLink;
import org.apache.sshd.sftp.client.packetdata.Version;
import org.apache.sshd.sftp.client.packetdata.Write;


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
        else if ( Stat.class.equals( interfaceType ) ) {
            return (T)newStat();
        }
        else if ( Attrs.class.equals( interfaceType ) ) {
            return (T)newAttrs();
        }
        else if ( Open.class.equals( interfaceType ) ) {
            return (T)newOpen();
        }
        else if ( Write.class.equals( interfaceType ) ) {
            return (T)newWrite();
        }
        else if ( Read.class.equals( interfaceType ) ) {
            return (T)newRead();
        }
        else if ( Data.class.equals( interfaceType ) ) {
            return (T)newData();
        }
        else if ( Rename.class.equals( interfaceType ) ) {
            return (T)newRename();
        }
        else if ( SymLink.class.equals( interfaceType ) ) {
            return (T)newSymLink();
        }
        throw new UnsupportedOperationException( interfaceType.getName() + " not yet implemented" );
    }

    public Attrs newAttrs() {
        return new DefaultPacketDataAttrs();
    }

    public Close newClose() {
        return new DefaultPacketDataClose();
    }
    
    public Data newData() {
        return new DefaultPacketDataData();
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

    public Open newOpen() {
        return new DefaultPacketDataOpen();
    }

    public OpenDir newOpenDir() {
        return new DefaultPacketDataOpenDir();
    }

    public Read newRead() {
        return new DefaultPacketDataRead();
    }

    public ReadDir newReadDir() {
        return new DefaultPacketDataReadDir();
    }
    
    public Rename newRename() {
        return new DefaultPacketDataRename();
    }

    public Stat newStat() {
        return new DefaultPacketDataStat();
    }

    public Status newStatus() {
        return new DefaultPacketDataStatus();
    }
    
    public SymLink newSymLink() {
        return new DefaultPacketDataSymLink();
    }

    public Version newVersion() {
        return new DefaultPacketDataVersion();
    }

    public Write newWrite() {
        return new DefaultPacketDataWrite();
    }
}
