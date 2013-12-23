package org.apache.sftp.protocol.client;


import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


import org.apache.sftp.protocol.NegotiatedVersion;
import org.apache.sftp.protocol.Request;
import org.apache.sftp.protocol.Response;
import org.apache.sftp.protocol.StatusException;
import org.apache.sftp.protocol.UnexpectedPacketDataException;
import org.apache.sftp.protocol.packetdata.Handle;


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

    public <T extends Request<T, Handle>> CloseableHandle requestCloseable( Request<T, Handle> request ) throws IOException, InterruptedException, StatusException, ExecutionException, UnexpectedPacketDataException;
}
