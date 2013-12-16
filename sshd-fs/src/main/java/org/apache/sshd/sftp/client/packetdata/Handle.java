package org.apache.sshd.sftp.client.packetdata;


import org.apache.sshd.sftp.Response;


public interface Handle extends Response<Handle> {
    public String getHandle();

    public Handle setHandle( String handle );
}
