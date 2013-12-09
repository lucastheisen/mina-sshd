package org.apache.sshd.sftp;


import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.Future;


public interface RequestProcessor extends Closeable {
    public int negotiatedVersion();

    public Future<? extends Response> request( Request request ) throws IOException;
}
