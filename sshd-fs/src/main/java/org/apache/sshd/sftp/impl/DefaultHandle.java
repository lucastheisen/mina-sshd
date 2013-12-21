package org.apache.sshd.sftp.impl;


import java.io.IOException;
import java.util.concurrent.ExecutionException;


import org.apache.sshd.sftp.Handle;
import org.apache.sshd.sftp.RequestProcessor;
import org.apache.sshd.sftp.StatusException;
import org.apache.sshd.sftp.client.packetdata.Close;
import org.apache.sshd.sftp.client.packetdata.Status;
import org.apache.sshd.sftp.client.packetdata.Status.Code;


public class DefaultHandle implements Handle {
    private org.apache.sshd.sftp.client.packetdata.Handle handle;
    private RequestProcessor requestProcessor;

    DefaultHandle( RequestProcessor requestProcessor, org.apache.sshd.sftp.client.packetdata.Handle handle ) {
        this.requestProcessor = requestProcessor;
        this.handle = handle;
    }

    @Override
    public org.apache.sshd.sftp.client.packetdata.Handle getHandle() {
        return handle;
    }

    @Override
    public void close() throws IOException {
        try {
            Status status = requestProcessor.request(
                    requestProcessor.newRequest( Close.class ).setHandle( handle.getHandle() ) ).get();
            if ( status.getCode() != Code.SSH_FX_OK ) {
                throw new IOException( new StatusException( status ) );
            }
        }
        catch ( ExecutionException e ) {
            throw new IOException( e.getCause().getMessage(), e.getCause() );
        }
        catch ( InterruptedException e ) {
            throw new IOException( e.getMessage(), e );
        }
    }

    @Override
    public String toString() {
        return "{'handle':'" + handle.toString() + "'}";
    }
}
