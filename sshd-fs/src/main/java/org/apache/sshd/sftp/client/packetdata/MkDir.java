package org.apache.sshd.sftp.client.packetdata;

import org.apache.sshd.sftp.FileAttributes;
import org.apache.sshd.sftp.Request;

public interface MkDir extends Request {
    public FileAttributes getFileAttributes();
    
    public String getPath();
    
    public MkDir setFileAttributes();
    
    public MkDir setPath( String path );
}
