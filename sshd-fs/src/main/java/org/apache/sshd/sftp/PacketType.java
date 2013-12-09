package org.apache.sshd.sftp;

import java.util.HashMap;
import java.util.Map;

public enum PacketType {
    SSH_FXP_INIT( (byte)1 ),
    SSH_FXP_VERSION( (byte)2 ),
    SSH_FXP_OPEN( (byte)3 ),
    SSH_FXP_CLOSE( (byte)4 ),
    SSH_FXP_READ( (byte)5 ),
    SSH_FXP_WRITE( (byte)6 ),
    SSH_FXP_LSTAT( (byte)7 ),
    SSH_FXP_FSTAT( (byte)8 ),
    SSH_FXP_SETSTAT( (byte)9 ),
    SSH_FXP_FSETSTAT( (byte)10 ),
    SSH_FXP_OPENDIR( (byte)11 ),
    SSH_FXP_READDIR( (byte)12 ),
    SSH_FXP_REMOVE( (byte)13 ),
    SSH_FXP_MKDIR( (byte)14 ),
    SSH_FXP_RMDIR( (byte)15 ),
    SSH_FXP_REALPATH( (byte)16 ),
    SSH_FXP_STAT( (byte)17 ),
    SSH_FXP_RENAME( (byte)18 ),
    SSH_FXP_READLINK( (byte)19 ),
    SSH_FXP_SYMLINK( (byte)20 ),
    SSH_FXP_STATUS( (byte)101 ),
    SSH_FXP_HANDLE( (byte)102 ),
    SSH_FXP_DATA( (byte)103 ),
    SSH_FXP_NAME( (byte)104 ),
    SSH_FXP_ATTRS( (byte)105 ),
    SSH_FXP_EXTENDED( (byte)200 ),
    SSH_FXP_EXTENDED_REPLY( (byte)201 );
    
    private static final Map<Byte, PacketType> lookup;
    
    static {
        lookup = new HashMap<>();
        for ( PacketType packetType : values() ) {
            lookup.put( packetType.typeValue, packetType );
        }
    }
    
    private byte typeValue;
    
    private PacketType( byte typeValue ) {
        this.typeValue = typeValue;
    }
    
    public byte getValue() {
        return typeValue;
    }
    
    public static PacketType fromValue( byte value ) {
        return lookup.get( value );
    }
}
