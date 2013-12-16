package org.apache.sshd.sftp;


import java.io.Closeable;


public interface Handle extends Closeable {
    public org.apache.sshd.sftp.client.packetdata.Handle getHandle();
}
