package org.apache.sshd.sftp.client;


import java.io.Closeable;
import java.io.IOException;


import org.apache.sshd.sftp.NegotiatedVersion;
import org.apache.sshd.sftp.StatusException;
import org.apache.sshd.sftp.Handle;


public interface SftpClient extends Closeable {
    public NegotiatedVersion negotiatedVersion();

    public Handle openDir( String path ) throws StatusException, IOException;
}
