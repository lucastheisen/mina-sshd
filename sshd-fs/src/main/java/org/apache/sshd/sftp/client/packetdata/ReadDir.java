package org.apache.sshd.sftp.client.packetdata;


import org.apache.sshd.sftp.Request;


public interface ReadDir extends Request<ReadDir> {
    public String getHandle();

    public ReadDir setHandle( String handle );
}
