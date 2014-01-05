package org.apache.sftp.protocol.impl;


import java.util.HashMap;
import java.util.Map;


import org.apache.sftp.protocol.PacketData;
import org.apache.sftp.protocol.PacketDataFactory;
import org.apache.sftp.protocol.PacketType;
import org.apache.sftp.protocol.Response;
import org.apache.sftp.protocol.packetdata.Attrs;
import org.apache.sftp.protocol.packetdata.Close;
import org.apache.sftp.protocol.packetdata.Data;
import org.apache.sftp.protocol.packetdata.Extended;
import org.apache.sftp.protocol.packetdata.ExtendedImplementation;
import org.apache.sftp.protocol.packetdata.ExtendedReply;
import org.apache.sftp.protocol.packetdata.FSetStat;
import org.apache.sftp.protocol.packetdata.FStat;
import org.apache.sftp.protocol.packetdata.Handle;
import org.apache.sftp.protocol.packetdata.Implementation;
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
import org.apache.sftp.protocol.packetdata.impl.DefaultAttrs;
import org.apache.sftp.protocol.packetdata.impl.DefaultClose;
import org.apache.sftp.protocol.packetdata.impl.DefaultData;
import org.apache.sftp.protocol.packetdata.impl.DefaultFSetStat;
import org.apache.sftp.protocol.packetdata.impl.DefaultFStat;
import org.apache.sftp.protocol.packetdata.impl.DefaultHandle;
import org.apache.sftp.protocol.packetdata.impl.DefaultInit;
import org.apache.sftp.protocol.packetdata.impl.DefaultLStat;
import org.apache.sftp.protocol.packetdata.impl.DefaultMkDir;
import org.apache.sftp.protocol.packetdata.impl.DefaultName;
import org.apache.sftp.protocol.packetdata.impl.DefaultOpen;
import org.apache.sftp.protocol.packetdata.impl.DefaultOpenDir;
import org.apache.sftp.protocol.packetdata.impl.DefaultRead;
import org.apache.sftp.protocol.packetdata.impl.DefaultReadDir;
import org.apache.sftp.protocol.packetdata.impl.DefaultReadLink;
import org.apache.sftp.protocol.packetdata.impl.DefaultRealPath;
import org.apache.sftp.protocol.packetdata.impl.DefaultRemove;
import org.apache.sftp.protocol.packetdata.impl.DefaultRename;
import org.apache.sftp.protocol.packetdata.impl.DefaultRmDir;
import org.apache.sftp.protocol.packetdata.impl.DefaultSetStat;
import org.apache.sftp.protocol.packetdata.impl.DefaultStat;
import org.apache.sftp.protocol.packetdata.impl.DefaultStatus;
import org.apache.sftp.protocol.packetdata.impl.DefaultSymLink;
import org.apache.sftp.protocol.packetdata.impl.DefaultVersion;
import org.apache.sftp.protocol.packetdata.impl.DefaultWrite;
import org.apache.sftp.protocol.packetdata.openssh.FStatVfs;
import org.apache.sftp.protocol.packetdata.openssh.FSync;
import org.apache.sftp.protocol.packetdata.openssh.HardLink;
import org.apache.sftp.protocol.packetdata.openssh.PosixRename;
import org.apache.sftp.protocol.packetdata.openssh.StatVfs;
import org.apache.sftp.protocol.packetdata.openssh.StatVfsReply;
import org.apache.sftp.protocol.packetdata.openssh.impl.DefaultFStatVfs;
import org.apache.sftp.protocol.packetdata.openssh.impl.DefaultFSync;
import org.apache.sftp.protocol.packetdata.openssh.impl.DefaultHardLink;
import org.apache.sftp.protocol.packetdata.openssh.impl.DefaultPosixRename;
import org.apache.sftp.protocol.packetdata.openssh.impl.DefaultStatVfs;
import org.apache.sftp.protocol.packetdata.openssh.impl.DefaultStatVfsReply;


public class DefaultPacketDataFactory implements PacketDataFactory {
    private Map<Object, Implementation<?>> implementations;

    public DefaultPacketDataFactory() {
        this.implementations = new HashMap<>();

        registerImplementation( Init.class, DefaultInit.class );
        registerImplementation( Version.class, DefaultVersion.class );
        registerImplementation( Handle.class, DefaultHandle.class );
        registerImplementation( OpenDir.class, DefaultOpenDir.class );
        registerImplementation( Status.class, DefaultStatus.class );
        registerImplementation( Close.class, DefaultClose.class );
        registerImplementation( ReadDir.class, DefaultReadDir.class );
        registerImplementation( Name.class, DefaultName.class );
        registerImplementation( Stat.class, DefaultStat.class );
        registerImplementation( Attrs.class, DefaultAttrs.class );
        registerImplementation( Open.class, DefaultOpen.class );
        registerImplementation( Write.class, DefaultWrite.class );
        registerImplementation( Read.class, DefaultRead.class );
        registerImplementation( Data.class, DefaultData.class );
        registerImplementation( Rename.class, DefaultRename.class );
        registerImplementation( SymLink.class, DefaultSymLink.class );
        registerImplementation( FSetStat.class, DefaultFSetStat.class );
        registerImplementation( LStat.class, DefaultLStat.class );
        registerImplementation( FStat.class, DefaultFStat.class );
        registerImplementation( MkDir.class, DefaultMkDir.class );
        registerImplementation( RmDir.class, DefaultRmDir.class );
        registerImplementation( ReadLink.class, DefaultReadLink.class );
        registerImplementation( RealPath.class, DefaultRealPath.class );
        registerImplementation( Remove.class, DefaultRemove.class );
        registerImplementation( SetStat.class, DefaultSetStat.class );

        // TODO: find a better way of obtaining the extendedRequest
        // it is available via instance method, but instantiation is silly,
        // so perhaps annotation?  not sure...
        registerExtendedImplementation( PosixRename.class, DefaultPosixRename.class,
                "posix-rename@openssh.com", null, null );
        registerExtendedImplementation( StatVfs.class, DefaultStatVfs.class,
                "statvfs@openssh.com", StatVfsReply.class, DefaultStatVfsReply.class );
        registerExtendedImplementation( FStatVfs.class, DefaultFStatVfs.class,
                "fstatvfs@openssh.com", StatVfsReply.class, DefaultStatVfsReply.class );
        registerExtendedImplementation( HardLink.class, DefaultHardLink.class,
                "hardlink@openssh.com", null, null );
        registerExtendedImplementation( FSync.class, DefaultFSync.class,
                "fsync@openssh.com", null, null );
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Extended<T, S>, S extends Response<S>> T newExtended( String extendedRequest ) {
        if ( implementations.containsKey( extendedRequest ) ) {
            return (T)newInstance( implementations.get( extendedRequest ) );
        }
        throw new UnsupportedOperationException( extendedRequest + " not implemented" );
    }

    private <T extends PacketData<T>> Implementation<T> newImplementation(
            final Class<T> interfaceType, final Class<? extends T> implementationType ) {
        return new Implementation<T>() {
            @Override
            public Class<? extends T> getImplementationType() {
                return implementationType;
            }

            @Override
            public Class<T> getInterfaceType() {
                return interfaceType;
            }
        };
    }

    private <T extends PacketData<T>, S extends T> T newInstance( Implementation<T> implementation ) {
        try {
            return implementation.getInterfaceType().cast( implementation.getImplementationType().newInstance() );
        }
        catch ( InstantiationException | IllegalAccessException e ) {
            throw new UnsupportedOperationException(
                    "Unable to create " + implementation.getInterfaceType().getName(), e );
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends PacketData<T>> T newPacketData( PacketType packetType ) {
        return newPacketData( (Class<T>)packetType.getInterface() );
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends PacketData<T>> T newPacketData( Class<T> interfaceType ) {
        if ( interfaceType.isAssignableFrom( Extended.class ) ||
                interfaceType.isAssignableFrom( ExtendedReply.class ) ) {
            if ( implementations.containsKey( interfaceType ) ) {
                return (T)newInstance( implementations.get( interfaceType ) );
            }
        }
        else {
            if ( implementations.containsKey( interfaceType ) ) {
                return (T)newInstance( implementations.get( interfaceType ) );
            }
        }
        throw new UnsupportedOperationException( interfaceType.getName() + " not implemented" );
    }

    private <T extends Extended<T, S>, S extends Response<S>, R extends ExtendedReply<R>> void registerExtendedImplementation(
            final Class<T> interfaceType, final Class<? extends T> implementationType, final String extendedRequest,
            final Class<R> extendedReplyInterfaceType, final Class<? extends R> extendedReplyImplementationType ) {
        registerExtendedImplementation( new ExtendedImplementation<T, S>() {
            @Override
            public Class<? extends T> getImplementationType() {
                return implementationType;
            }

            @Override
            public Class<T> getInterfaceType() {
                return interfaceType;
            }

            @Override
            public String getExtendedRequest() {
                return extendedRequest;
            }

            @SuppressWarnings("unchecked")
            @Override
            public Implementation<S> getExtendedReplyImplementation() {
                return (Implementation<S>)newImplementation( extendedReplyInterfaceType, extendedReplyImplementationType );
            }
        } );
    }

    private <T extends Extended<T, S>, S extends Response<S>> void registerExtendedImplementation(
            ExtendedImplementation<T, S> extension ) {
        this.implementations.put( extension.getExtendedRequest(), extension );
        this.implementations.put( extension.getInterfaceType(), extension );

        Implementation<S> extendedReply = extension.getExtendedReplyImplementation();
        if ( extendedReply != null ) {
            this.implementations.put( extendedReply.getInterfaceType(), extendedReply );
        }
    }

    private <T extends PacketData<T>> void registerImplementation(
            final Class<T> interfaceType, final Class<? extends T> implementationType ) {
        registerImplementation( newImplementation( interfaceType, implementationType ) );
    }

    private <T extends PacketData<T>> void registerImplementation(
            Implementation<T> implementation ) {
        implementations.put( implementation.getInterfaceType(), implementation );
    }
}
