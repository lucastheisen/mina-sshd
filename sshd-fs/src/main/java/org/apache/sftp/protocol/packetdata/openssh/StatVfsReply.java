package org.apache.sftp.protocol.packetdata.openssh;


import java.util.EnumSet;


import org.apache.sftp.protocol.impl.Maskable;
import org.apache.sftp.protocol.packetdata.ExtendedReply;


/**
 * Implements the reply to a <code>statvfs</code> command:
 * 
 * <pre>
 * 3.4. sftp: Extension requests "statvfs@openssh.com" and
 *          "fstatvfs@openssh.com"
 * 
 * These requests correspond to the statvfs and fstatvfs POSIX system
 * interfaces. The "statvfs@openssh.com" request operates on an explicit
 * pathname, and is formatted as follows:
 * 
 *         uint32          id
 *         string          "statvfs@openssh.com"
 *         string          path
 * 
 * The "fstatvfs@openssh.com" operates on an open file handle:
 * 
 *         uint32          id
 *         string          "fstatvfs@openssh.com"
 *         string          handle
 * 
 * These requests return a SSH_FXP_STATUS reply on failure. On success they
 * return the following SSH_FXP_EXTENDED_REPLY reply:
 * 
 *         uint32          id
 *         uint64          f_bsize         /* file system block size *&#47;
 *         uint64          f_frsize        /* fundamental fs block size *&#47;
 *         uint64          f_blocks        /* number of blocks (unit f_frsize) *&#47;
 *         uint64          f_bfree         /* free blocks in file system *&#47;
 *         uint64          f_bavail        /* free blocks for non-root *&#47;
 *         uint64          f_files         /* total file inodes *&#47;
 *         uint64          f_ffree         /* free file inodes *&#47;
 *         uint64          f_favail        /* free file inodes for to non-root *&#47;
 *         uint64          f_fsid          /* file system id *&#47;
 *         uint64          f_flag          /* bit mask of f_flag values *&#47;
 *         uint64          f_namemax       /* maximum filename length *&#47;
 * 
 * The values of the f_flag bitmask are as follows:
 * 
 *         #define SSH_FXE_STATVFS_ST_RDONLY       0x1     /* read-only *&#47;
 *         #define SSH_FXE_STATVFS_ST_NOSUID       0x2     /* no setuid *&#47;
 * 
 * Both the "statvfs@openssh.com" and "fstatvfs@openssh.com" extensions are
 * advertised in the SSH_FXP_VERSION hello with version "2".
 * </pre>
 * 
 * @author Lucas Theisen
 * @see <a href=
 *      "http://www.openbsd.org/cgi-bin/cvsweb/src/usr.bin/ssh/PROTOCOL?rev=HEAD;content-type=text/plain"
 *      >OpenSSH's deviations and extensions to the published SSH protocol</a>
 */
public interface StatVfsReply extends ExtendedReply<StatVfsReply> {

    /**
     * f_bavail: free blocks for non-root
     */
    public long getBlocksAvailable();

    /**
     * f_bfree: free blocks in file system
     */
    public long getBlocksFree();

    /**
     * f_bsize: file system block size
     */
    public long getBlockSize();

    /**
     * f_favail: free file inodes for to non-root
     */
    public long getFilesAvailable();

    /**
     * f_ffree: free file inodes
     */
    public long getFilesFree();

    /**
     * f_fsid: file system id
     */
    public long getFileSid();

    /**
     * f_flag: bit mask of f_flag values
     */
    public EnumSet<Flag> getFlags();

    /**
     * f_frsize: fundamental fs block size
     */
    public long getFundamentalBlockSize();

    /**
     * f_namemax: maximum filename length
     */
    public long getNameMaxLength();

    /**
     * f_blocks: number of blocks (unit f_frsize)
     */
    public long getTotalBlocks();

    /**
     * f_files: total file inodes
     */
    public long getTotalFiles();

    /**
     * f_bavail: free blocks for non-root
     */
    public void setBlocksAvailable( long blocksAvailable );

    /**
     * f_bfree: free blocks in file system
     */
    public void setBlocksFree( long blocksFree );

    /**
     * f_bsize: file system block size
     */
    public void setBlockSize( long blockSize );

    /**
     * f_favail: free file inodes for to non-root
     */
    public void setFilesAvailable( long filesAvailable );

    /**
     * f_ffree: free file inodes
     */
    public void setFilesFree( long filesFree );

    /**
     * f_fsid: file system id
     */
    public void setFileSid( long fileSid );

    /**
     * f_flag: bit mask of f_flag values
     */
    public void setFlags( EnumSet<Flag> flags );

    /**
     * f_frsize: fundamental fs block size
     */
    public void setFundamentalBlockSize( long fundamentalBlockSize );

    /**
     * f_namemax: maximum filename length
     */
    public void setNameMaxLength( long nameMaxLength );

    /**
     * f_blocks: number of blocks (unit f_frsize)
     */
    public void setTotalBlocks( long totalBlocks );

    /**
     * f_files: total file inodes
     */
    public void setTotalFiles( long totalFiles );

    public static enum Flag implements Maskable<Flag> {
        SSH_FXE_STATVFS_ST_RDONLY(0x1),
        SSH_FXE_STATVFS_ST_NOSUID(0x2);

        private long value;

        private Flag( long value ) {
            this.value = value;
        }

        @Override
        public long getValue() {
            return value;
        }
    }
}
