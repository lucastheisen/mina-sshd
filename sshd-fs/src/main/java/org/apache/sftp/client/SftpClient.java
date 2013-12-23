package org.apache.sftp.client;


import java.io.Closeable;


import org.apache.sftp.protocol.NegotiatedVersion;


public interface SftpClient extends Closeable {
    public NegotiatedVersion negotiatedVersion();
}
