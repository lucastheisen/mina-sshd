package org.apache.sshd.sftp.client.packetdata;


import org.apache.sshd.sftp.Request;


public interface Close extends Request<Close> {
    public String getHandle();

    public Close setHandle( String handle );
}
