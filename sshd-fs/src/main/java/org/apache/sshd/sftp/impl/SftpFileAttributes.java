package org.apache.sshd.sftp.impl;


import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.PosixFilePermission;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class SftpFileAttributes {
    private Map<String, String> extended = null;
    private EnumSet<Flag> flags = null;
    private int gid = -1;
    private FileTime lastAccessTime = null;
    private FileTime lastModifiedTime = null;
    private EnumSet<Permission> permissions = null;
    private long size = -1;
    private int uid = -1;

    public SftpFileAttributes() {
        extended = new HashMap<>();
    }

    public SftpFileAttributes addExtended( String type, String data ) {
        if ( extended == null ) {
            extended = new HashMap<>();
        }
        extended.put( type, data );
        flags.add( Flag.SSH_FILEXFER_ATTR_EXTENDED );
        return this;
    }

    private EnumSet<Flag> flags() {
        if ( flags == null ) {
            flags = EnumSet.noneOf( Flag.class );
        }
        return flags;
    }

    public int getGid() {
        return gid;
    }

    public FileTime getLastAccessTime() {
        return lastAccessTime;
    }

    public FileTime getLastModifiedTime() {
        return lastModifiedTime;
    }

    public EnumSet<Permission> getPermissions() {
        return permissions;
    }

    public int getUid() {
        return uid;
    }

    public long getSize() {
        return size;
    }

    public SftpFileAttributes parseFrom( SftpProtocolBuffer buffer ) {
        flags = MaskFactory.fromMask( buffer.getInt(), Flag.class );
        if ( flags.contains( Flag.SSH_FILEXFER_ATTR_SIZE ) ) {
            size = buffer.getLong();
        }
        if ( flags.contains( Flag.SSH_FILEXFER_ATTR_UIDGID ) ) {
            uid = buffer.getInt();
            gid = buffer.getInt();
        }
        if ( flags.contains( Flag.SSH_FILEXFER_ATTR_PERMISSIONS ) ) {
            permissions = MaskFactory.fromMask( buffer.getInt(), Permission.class );
        }
        if ( flags.contains( Flag.SSH_FILEXFER_ATTR_ACMODTIME ) ) {
            lastAccessTime = FileTime.from( buffer.getInt(), TimeUnit.SECONDS );
            lastModifiedTime = FileTime.from( buffer.getInt(), TimeUnit.SECONDS );
        }
        if ( flags.contains( Flag.SSH_FILEXFER_ATTR_EXTENDED ) ) {
            int count = buffer.getInt();
            for ( int i = 0; i < count; i++ ) {
                extended.put( buffer.getString(), buffer.getString() );
            }
        }

        return this;
    }

    public SftpFileAttributes setExtended( Map<String, String> extended ) {
        if ( extended == null ) {
            this.extended = null;
            flags().remove( Flag.SSH_FILEXFER_ATTR_EXTENDED );
        }
        else {
            this.extended = extended;
            flags().add( Flag.SSH_FILEXFER_ATTR_EXTENDED );
        }
        return this;
    }

    public SftpFileAttributes setPermissions( Permission... permissions ) {
        if ( permissions == null || permissions.length == 0 ) {
            return setPermissions( (EnumSet<Permission>)null );
        }
        else {
            return setPermissions( EnumSet.copyOf( Arrays.asList( permissions ) ) );
        }
    }

    public SftpFileAttributes setPermissions( EnumSet<Permission> permissions ) {
        if ( permissions == null || permissions.isEmpty() ) {
            this.permissions = null;
            flags().remove( Flag.SSH_FILEXFER_ATTR_PERMISSIONS );
        }
        else {
            this.permissions = permissions;
            flags().add( Flag.SSH_FILEXFER_ATTR_PERMISSIONS );
        }
        return this;
    }

    public SftpFileAttributes setSize( int size ) {
        if ( size < 0 ) {
            this.size = -1;
            flags().remove( Flag.SSH_FILEXFER_ATTR_SIZE );
        }
        else {
            this.size = size;
            flags().add( Flag.SSH_FILEXFER_ATTR_SIZE );
        }
        return this;
    }

    public SftpFileAttributes setTimes( FileTime lastAccessTime, FileTime lastModifiedTime ) {
        if ( lastAccessTime == null && lastModifiedTime == null ) {
            this.lastAccessTime = null;
            this.lastModifiedTime = null;
            flags().remove( Flag.SSH_FILEXFER_ATTR_ACMODTIME );
        }
        else if ( lastAccessTime == null || lastModifiedTime == null ) {
            throw new IllegalArgumentException( "all times must be null or all times must be non-null" );
        }
        else {
            this.lastAccessTime = lastAccessTime;
            this.lastModifiedTime = lastModifiedTime;
            flags().add( Flag.SSH_FILEXFER_ATTR_ACMODTIME );
        }
        return this;
    }

    public SftpFileAttributes setUidGid( int uid, int gid ) {
        if ( uid < 0 && gid < 0 ) {
            this.uid = -1;
            this.gid = -1;
            flags().remove( Flag.SSH_FILEXFER_ATTR_UIDGID );
        }
        else if ( uid < 0 || gid < 0 ) {
            throw new IllegalArgumentException( "both uid and gid must be greater than zero or both must be less than zero" );
        }
        else {
            this.uid = uid;
            this.gid = gid;
            flags().add( Flag.SSH_FILEXFER_ATTR_UIDGID );
        }
        return this;
    }

    public void writeTo( SftpProtocolBuffer buffer ) {
        // ensure flags was initialized 
        EnumSet<Flag> flags = flags();

        buffer.putInt( MaskFactory.toMask( flags ) );
        if ( flags.contains( Flag.SSH_FILEXFER_ATTR_SIZE ) ) {
            buffer.putLong( size );
        }
        if ( flags.contains( Flag.SSH_FILEXFER_ATTR_UIDGID ) ) {
            buffer.putInt( uid );
            buffer.putInt( gid );
        }
        if ( flags.contains( Flag.SSH_FILEXFER_ATTR_PERMISSIONS ) ) {
            buffer.putInt( MaskFactory.toMask( permissions ) );
        }
        if ( flags.contains( Flag.SSH_FILEXFER_ATTR_ACMODTIME ) ) {
            buffer.putInt( (int)lastAccessTime.to( TimeUnit.SECONDS ) );
            buffer.putInt( (int)lastModifiedTime.to( TimeUnit.SECONDS ) );
        }
        if ( flags.contains( Flag.SSH_FILEXFER_ATTR_EXTENDED ) ) {
            buffer.putInt( extended.size() );
            for ( Map.Entry<String, String> entry : extended.entrySet() ) {
                buffer.putString( entry.getKey() );
                buffer.putString( entry.getValue() );
            }
        }
    }

    private enum Flag implements Maskable<Flag> {
        SSH_FILEXFER_ATTR_SIZE(0x00000001),
        SSH_FILEXFER_ATTR_UIDGID(0x00000002),
        SSH_FILEXFER_ATTR_PERMISSIONS(0x00000004),
        SSH_FILEXFER_ATTR_ACMODTIME(0x00000008),
        SSH_FILEXFER_ATTR_EXTENDED(0x80000000);

        private int value;

        private Flag( int value ) {
            this.value = value;
        }

        @Override
        public int getValue() {
            return value;
        }
    }

    public enum Permission implements Maskable<Permission> {
        S_IFMT(0170000, "bitmask for the file type bitfields", null),
        S_IFSOCK(0140000, "socket", null),
        S_IFLNK(0120000, "symbolic link", null),
        S_IFREG(0100000, "regular file", null),
        S_IFBLK(0060000, "block device", null),
        S_IFDIR(0040000, "directory", null),
        S_IFCHR(0020000, "character device", null),
        S_IFIFO(0010000, "fifo ", null),
        S_ISUID(0004000, "set UID bit", null),
        S_ISGID(0002000, "set GID bit ", null),
        S_ISVTX(0001000, "sticky bit", null),
        S_IRWXU(0000700, "mask for file owner permissions", null),
        S_IRUSR(0000400, "owner has read permission", PosixFilePermission.OWNER_READ),
        S_IWUSR(0000200, "owner has write permission", PosixFilePermission.OWNER_WRITE),
        S_IXUSR(0000100, "owner has execute permission", PosixFilePermission.OWNER_EXECUTE),
        S_IRWXG(0000070, "mask for group permissions", null),
        S_IRGRP(0000040, "group has read permission", PosixFilePermission.GROUP_READ),
        S_IWGRP(0000020, "group has write permission", PosixFilePermission.GROUP_WRITE),
        S_IXGRP(0000010, "group has execute permission", PosixFilePermission.GROUP_EXECUTE),
        S_IRWXO(0000007, "mask for permissions for others (not in group)", null),
        S_IROTH(0000004, "others have read permission", PosixFilePermission.OTHERS_READ),
        S_IWOTH(0000002, "others have write permisson", PosixFilePermission.OTHERS_WRITE),
        S_IXOTH(0000001, "others have execute permission", PosixFilePermission.OTHERS_EXECUTE);

        private int value;
        private String description;
        private PosixFilePermission posixFilePermission;

        private Permission( int value, String description, PosixFilePermission posixFilePermission ) {
            this.value = value;
            this.description = description;
            this.posixFilePermission = posixFilePermission;
        }

        public String getDescription() {
            return description;
        }

        @Override
        public int getValue() {
            return value;
        }

        public static EnumSet<PosixFilePermission> toPosixFilePermissions( EnumSet<Permission> permissions ) {
            List<PosixFilePermission> posixFilePermissions = new ArrayList<>();
            for ( Permission permission : permissions ) {
                if ( permission.posixFilePermission != null ) {
                    posixFilePermissions.add( permission.posixFilePermission );
                }
            }
            return EnumSet.copyOf( posixFilePermissions );
        }
    }
}