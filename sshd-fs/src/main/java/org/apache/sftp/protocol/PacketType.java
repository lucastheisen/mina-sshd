package org.apache.sftp.protocol;

import java.util.HashMap;
import java.util.Map;


import org.apache.sftp.protocol.packetdata.Attrs;
import org.apache.sftp.protocol.packetdata.Close;
import org.apache.sftp.protocol.packetdata.Data;
import org.apache.sftp.protocol.packetdata.Extended;
import org.apache.sftp.protocol.packetdata.ExtendedReply;
import org.apache.sftp.protocol.packetdata.FSetStat;
import org.apache.sftp.protocol.packetdata.FStat;
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

public enum PacketType {
    SSH_FXP_INIT( 1, Init.class ),
    SSH_FXP_VERSION( 2, Version.class ),
    SSH_FXP_OPEN( 3, Open.class ),
    SSH_FXP_CLOSE( 4, Close.class ),
    SSH_FXP_READ( 5, Read.class ),
    SSH_FXP_WRITE( 6, Write.class ),
    SSH_FXP_LSTAT( 7, LStat.class ),
    SSH_FXP_FSTAT( 8, FStat.class ),
    SSH_FXP_SETSTAT( 9, SetStat.class ),
    SSH_FXP_FSETSTAT( 10, FSetStat.class ),
    SSH_FXP_OPENDIR( 11, OpenDir.class ),
    SSH_FXP_READDIR( 12, ReadDir.class ),
    SSH_FXP_REMOVE( 13, Remove.class ),
    SSH_FXP_MKDIR( 14, MkDir.class ),
    SSH_FXP_RMDIR( 15, RmDir.class ),
    SSH_FXP_REALPATH( 16, RealPath.class ),
    SSH_FXP_STAT( 17, Stat.class ),
    SSH_FXP_RENAME( 18, Rename.class ),
    SSH_FXP_READLINK( 19, ReadLink.class ),
    SSH_FXP_SYMLINK( 20, SymLink.class ),
    SSH_FXP_STATUS( 101, Status.class ),
    SSH_FXP_HANDLE( 102, org.apache.sftp.protocol.packetdata.Handle.class ),
    SSH_FXP_DATA( 103, Data.class ),
    SSH_FXP_NAME( 104, Name.class ),
    SSH_FXP_ATTRS( 105, Attrs.class ),
    SSH_FXP_EXTENDED( 200, Extended.class ),
    SSH_FXP_EXTENDED_REPLY( 201, ExtendedReply.class );
    
    private static final Map<Byte, PacketType> lookup;
    
    static {
        lookup = new HashMap<>();
        for ( PacketType packetType : values() ) {
            lookup.put( packetType.typeValue, packetType );
        }
    }
    
    private Class<?> clazz;
    private byte typeValue;
    
    private PacketType( int typeValue, Class<?> clazz ) {
        this.typeValue = (byte)typeValue;
        this.clazz = clazz;
    }
    
    @SuppressWarnings("unchecked")
    public <T extends PacketData<T>> Class<T> getInterface() {
        return (Class<T>)clazz;
    }
    
    public byte getValue() {
        return typeValue;
    }
    
    public static PacketType fromValue( byte value ) {
        return lookup.get( value );
    }
}
