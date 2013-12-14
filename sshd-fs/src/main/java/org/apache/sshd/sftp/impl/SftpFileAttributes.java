package org.apache.sshd.sftp.impl;


import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.GroupPrincipal;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.UserPrincipal;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;


public class SftpFileAttributes implements PosixFileAttributes {
    private static final Charset UTF8 = Charset.forName( "UTF-8" );

    private FileTime creationTime;
    private boolean directory;
    private Object fileKey;
    private SftpGroupPrincipal groupPrincipal;
    private FileTime lastAccessTime;
    private FileTime lastModifiedTime;
    private boolean other;
    private Set<PosixFilePermission> permissions;
    private boolean regularFile;
    private long size;
    private boolean symbolicLink;
    private SftpUserPrincipal userPrincipal;

    @Override
    public FileTime creationTime() {
        return creationTime;
    }

    @Override
    public Object fileKey() {
        return fileKey;
    }

    @Override
    public SftpGroupPrincipal group() {
        return groupPrincipal;
    }

    @Override
    public boolean isDirectory() {
        return directory;
    }

    @Override
    public boolean isOther() {
        return other;
    }

    @Override
    public boolean isRegularFile() {
        return regularFile;
    }

    @Override
    public boolean isSymbolicLink() {
        return symbolicLink;
    }

    @Override
    public FileTime lastAccessTime() {
        return lastAccessTime;
    }

    @Override
    public FileTime lastModifiedTime() {
        return lastModifiedTime;
    }

    public SftpFileAttributes parseFrom( ByteBuffer buffer ) {
        EnumSet<Flag> flags = Flag.fromMask( buffer.getInt() );
        if ( flags.contains( Flag.SSH_FILEXFER_ATTR_SIZE ) ) {
            size = buffer.getLong();
        }
        if ( flags.contains( Flag.SSH_FILEXFER_ATTR_UIDGID ) ) {
            userPrincipal = new SftpUserPrincipal( buffer.getInt() );
            groupPrincipal = new SftpGroupPrincipal( buffer.getInt() );
        }
        if ( flags.contains( Flag.SSH_FILEXFER_ATTR_PERMISSIONS ) ) {
            int permissions = buffer.getInt();
        }
        if ( flags.contains( Flag.SSH_FILEXFER_ATTR_ACMODTIME ) ) {
            lastAccessTime = FileTime.from( buffer.getInt(), TimeUnit.SECONDS );
            lastModifiedTime = FileTime.from( buffer.getInt(), TimeUnit.SECONDS );
        }
        if ( flags.contains( Flag.SSH_FILEXFER_ATTR_EXTENDED ) ) {
            int count = buffer.getInt();
            byte[] bytes = null;
            String[] nameValue = new String[2];
            for ( int i = 0; i < count; i++ ) {
                for ( int j = 0; j < 2; j++ ) {
                    int size = buffer.getInt();
                    if ( bytes == null || bytes.length < size ) {
                        bytes = new byte[size];
                    }
                    buffer.get( bytes, 0, size );
                    nameValue[j] = new String( bytes, UTF8 );
                }
            }
        }
        
        return this;
    }

    @Override
    public Set<PosixFilePermission> permissions() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SftpUserPrincipal owner() {
        return userPrincipal;
    }

    @Override
    public long size() {
        return size;
    }

    public void writeTo( ByteBuffer buffer ) throws IOException {
        // TODO Auto-generated method stub
    }

    private enum Flag {
        SSH_FILEXFER_ATTR_SIZE(0x00000001),
        SSH_FILEXFER_ATTR_UIDGID(0x00000002),
        SSH_FILEXFER_ATTR_PERMISSIONS(0x00000004),
        SSH_FILEXFER_ATTR_ACMODTIME(0x00000008),
        SSH_FILEXFER_ATTR_EXTENDED(0x80000000);

        private int value;

        private Flag( int value ) {
            this.value = value;
        }
        
        public static EnumSet<Flag> fromMask( int mask ) {
            List<Flag> flags = new ArrayList<>();
            for ( Flag flag : values() ) {
                if ( (mask & flag.value) == flag.value ) {
                    flags.add( flag );
                }
            }
            return EnumSet.copyOf( flags );
        }
    }
    
    public static class SftpGroupPrincipal implements GroupPrincipal {
        private int gid;
        
        public SftpGroupPrincipal( int gid ) {
            this.gid = gid;
        }
        
        public int getGid() {
            return gid;
        }

        @Override
        public String getName() {
            return Integer.toString( gid );
        }
    }
    
    public static class SftpUserPrincipal implements UserPrincipal {
        private int uid;
        
        public SftpUserPrincipal( int uid ) {
            this.uid = uid;
        }
        
        @Override
        public String getName() {
            return Integer.toString( uid );
        }

        public int getUid() {
            return uid;
        }
    }
}
