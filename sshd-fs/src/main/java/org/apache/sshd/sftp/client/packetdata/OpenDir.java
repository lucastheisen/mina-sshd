package org.apache.sshd.sftp.client.packetdata;

import org.apache.sshd.sftp.Request;

public interface OpenDir extends Request {
    public String getPath();
    
    public void setPath( String path );
}
