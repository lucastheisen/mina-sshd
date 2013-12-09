package org.apache.sshd.sftp.client;

import java.io.Closeable;

public interface SftpClient extends Closeable {
    public int negotiatedVersion();
}
