package org.apache.sshd.sftp;


import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.Future;


public interface RequestProcessor extends Closeable {
    /**
     * Returns the NegotiatedVersion information from the initialization of this
     * RequestProcessor.
     * 
     * @return The negotiated version.
     */
    public NegotiatedVersion negotiatedVersion();

    /**
     * Creates a new Request of the specified type.
     * 
     * @param type
     *            The type of Request.
     * @return The new Request.
     */
    public <S extends Response<S>, T extends Request<T, S>> T newRequest( Class<T> type );

    public <S extends Response<S>, T extends Request<T, S>> Future<S> request( Request<T, S> request ) throws IOException;
}
