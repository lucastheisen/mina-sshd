package org.apache.sshd.sftp.client.packetdata;

import org.apache.sshd.sftp.Request;
import org.apache.sshd.sftp.impl.SftpFileAttributes;

public interface MkDir extends Request<MkDir> {
    public SftpFileAttributes getFileAttributes();
    
    public String getPath();
    
    public MkDir setFileAttributes( SftpFileAttributes sftpFileAttributes );
    
    public MkDir setPath( String path );
}
