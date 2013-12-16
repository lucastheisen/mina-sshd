package org.apache.sshd.sftp;

import java.util.HashMap;
import java.util.Map;


import org.apache.sshd.sftp.client.packetdata.Attrs;
import org.apache.sshd.sftp.client.packetdata.Close;
import org.apache.sshd.sftp.client.packetdata.Data;
import org.apache.sshd.sftp.client.packetdata.Extended;
import org.apache.sshd.sftp.client.packetdata.ExtendedReply;
import org.apache.sshd.sftp.client.packetdata.FSetStat;
import org.apache.sshd.sftp.client.packetdata.FStat;
import org.apache.sshd.sftp.client.packetdata.Init;
import org.apache.sshd.sftp.client.packetdata.LStat;
import org.apache.sshd.sftp.client.packetdata.MkDir;
import org.apache.sshd.sftp.client.packetdata.Name;
import org.apache.sshd.sftp.client.packetdata.Open;
import org.apache.sshd.sftp.client.packetdata.OpenDir;
import org.apache.sshd.sftp.client.packetdata.Read;
import org.apache.sshd.sftp.client.packetdata.ReadDir;
import org.apache.sshd.sftp.client.packetdata.ReadLink;
import org.apache.sshd.sftp.client.packetdata.RealPath;
import org.apache.sshd.sftp.client.packetdata.Remove;
import org.apache.sshd.sftp.client.packetdata.Rename;
import org.apache.sshd.sftp.client.packetdata.RmDir;
import org.apache.sshd.sftp.client.packetdata.SetStat;
import org.apache.sshd.sftp.client.packetdata.Stat;
import org.apache.sshd.sftp.client.packetdata.Status;
import org.apache.sshd.sftp.client.packetdata.SymLink;
import org.apache.sshd.sftp.client.packetdata.Version;
import org.apache.sshd.sftp.client.packetdata.Write;

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
    SSH_FXP_HANDLE( 102, org.apache.sshd.sftp.client.packetdata.Handle.class ),
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
