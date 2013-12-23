package org.apache.sftp.protocol.packetdata.impl;


import org.apache.sftp.protocol.PacketData;
import org.apache.sftp.protocol.PacketDataFactory;
import org.apache.sftp.protocol.PacketType;
import org.apache.sftp.protocol.packetdata.Attrs;
import org.apache.sftp.protocol.packetdata.Close;
import org.apache.sftp.protocol.packetdata.Data;
import org.apache.sftp.protocol.packetdata.Extended;
import org.apache.sftp.protocol.packetdata.ExtendedReply;
import org.apache.sftp.protocol.packetdata.FSetStat;
import org.apache.sftp.protocol.packetdata.FStat;
import org.apache.sftp.protocol.packetdata.Handle;
import org.apache.sftp.protocol.packetdata.Init;
import org.apache.sftp.protocol.packetdata.LStat;
import org.apache.sftp.protocol.packetdata.MkDir;
import org.apache.sftp.protocol.packetdata.Name;
import org.apache.sftp.protocol.packetdata.Open;
import org.apache.sftp.protocol.packetdata.OpenDir;
import org.apache.sftp.protocol.packetdata.Read;
import org.apache.sftp.protocol.packetdata.ReadDir;
import org.apache.sftp.protocol.packetdata.ReadLink;
import org.apache.sftp.protocol.packetdata.RealPath;
import org.apache.sftp.protocol.packetdata.Remove;
import org.apache.sftp.protocol.packetdata.Rename;
import org.apache.sftp.protocol.packetdata.RmDir;
import org.apache.sftp.protocol.packetdata.SetStat;
import org.apache.sftp.protocol.packetdata.Stat;
import org.apache.sftp.protocol.packetdata.Status;
import org.apache.sftp.protocol.packetdata.SymLink;
import org.apache.sftp.protocol.packetdata.Version;
import org.apache.sftp.protocol.packetdata.Write;


public class DefaultPacketDataFactory implements PacketDataFactory {
    @Override
    @SuppressWarnings("unchecked")
    public <T extends PacketData<T>> T newInstance( byte packetTypeByte ) {
        PacketType packetType = PacketType.fromValue( packetTypeByte );
        if ( packetType == null ) {
            return (T)new DefaultRaw( packetTypeByte );
        }

        try {
            return newInstance( (Class<T>)packetType.getInterface() );
        }
        catch ( UnsupportedOperationException e ) {
            return (T)new DefaultRaw( packetTypeByte );
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends PacketData<T>> T newInstance( Class<T> interfaceType ) {
        // TODO: prioritize most common first to reduce checks
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
        else if ( FSetStat.class.equals( interfaceType ) ) {
            return (T)newFSetStat();
        }
        else if ( LStat.class.equals( interfaceType ) ) {
            return (T)newLStat();
        }
        else if ( FStat.class.equals( interfaceType ) ) {
            return (T)newFStat();
        }
        else if ( MkDir.class.equals( interfaceType ) ) {
            return (T)newMkDir();
        }
        else if ( RmDir.class.equals( interfaceType ) ) {
            return (T)newRmDir();
        }
        else if ( ReadLink.class.equals( interfaceType ) ) {
            return (T)newReadLink();
        }
        else if ( RealPath.class.equals( interfaceType ) ) {
            return (T)newRealPath();
        }
        else if ( Remove.class.equals( interfaceType ) ) {
            return (T)newRemove();
        }
        else if ( SetStat.class.equals( interfaceType ) ) {
            return (T)newSetStat();
        }
        else if ( Extended.class.isAssignableFrom( interfaceType ) ) {
            return newExtended( interfaceType );
        }
        else if ( ExtendedReply.class.isAssignableFrom( interfaceType ) ) {
            return newExtendedReply( interfaceType );
        }
        throw new UnsupportedOperationException( interfaceType.getName() + " not yet implemented" );
    }

    public Attrs newAttrs() {
        return new DefaultAttrs();
    }

    public Close newClose() {
        return new DefaultClose();
    }

    public Data newData() {
        return new DefaultData();
    }

    public <T extends PacketData<T>> T newExtended( Class<T> interfaceType ) {
        throw new UnsupportedOperationException( interfaceType.getName() + " not yet implemented" );
    }

    public <T extends PacketData<T>> T newExtendedReply( Class<T> interfaceType ) {
        throw new UnsupportedOperationException( interfaceType.getName() + " not yet implemented" );
    }

    public FSetStat newFSetStat() {
        return new DefaultFSetStat();
    }

    public FStat newFStat() {
        return new DefaultFStat();
    }

    public Handle newHandle() {
        return new DefaultHandle();
    }

    public Init newInit() {
        return new DefaultInit();
    }

    public LStat newLStat() {
        return new DefaultLStat();
    }

    public MkDir newMkDir() {
        return new DefaultMkDir();
    }

    public Name newName() {
        return new DefaultName();
    }

    public Open newOpen() {
        return new DefaultOpen();
    }

    public OpenDir newOpenDir() {
        return new DefaultOpenDir();
    }

    public Read newRead() {
        return new DefaultRead();
    }

    public ReadDir newReadDir() {
        return new DefaultReadDir();
    }

    public ReadLink newReadLink() {
        return new DefaultReadLink();
    }

    public RealPath newRealPath() {
        return new DefaultRealPath();
    }

    public Remove newRemove() {
        return new DefaultRemove();
    }

    public Rename newRename() {
        return new DefaultRename();
    }

    public RmDir newRmDir() {
        return new DefaultRmDir();
    }

    public SetStat newSetStat() {
        return new DefaultSetStat();
    }

    public Stat newStat() {
        return new DefaultStat();
    }

    public Status newStatus() {
        return new DefaultStatus();
    }

    public SymLink newSymLink() {
        return new DefaultSymLink();
    }

    public Version newVersion() {
        return new DefaultVersion();
    }

    public Write newWrite() {
        return new DefaultWrite();
    }
}
