package org.apache.sshd.sftp.client.packetdata;


import org.apache.sshd.sftp.Request;


public interface OpenDir extends Request<OpenDir> {
    public String getPath();

    public OpenDir setPath( String path );
}
