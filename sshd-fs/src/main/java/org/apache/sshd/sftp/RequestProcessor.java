package org.apache.sshd.sftp;


import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.Future;


public interface RequestProcessor extends Closeable {
    public NegotiatedVersion negotiatedVersion();

    public <T extends Request<T>> Future<Response<?>> request( Request<T> request ) throws IOException;

    public <T extends Request<T>> T newRequest( Class<T> type );
}
