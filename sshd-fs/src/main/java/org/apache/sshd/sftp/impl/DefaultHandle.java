package org.apache.sshd.sftp.impl;


import java.io.IOException;
import java.util.concurrent.ExecutionException;


import org.apache.sshd.sftp.Handle;
import org.apache.sshd.sftp.RequestOrResponse;
import org.apache.sshd.sftp.RequestProcessor;
import org.apache.sshd.sftp.StatusException;
import org.apache.sshd.sftp.client.packetdata.Close;
import org.apache.sshd.sftp.client.packetdata.Status;
import org.apache.sshd.sftp.client.packetdata.Status.Code;


public class DefaultHandle implements Handle {
    private org.apache.sshd.sftp.client.packetdata.Handle handle;
    private RequestProcessor requestProcessor;

    public DefaultHandle( RequestProcessor requestProcessor, org.apache.sshd.sftp.client.packetdata.Handle handle ) {
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
            RequestOrResponse<?> response = requestProcessor.request( 
                    requestProcessor.newRequest( Close.class ).setHandle( handle.getHandle() ) ).get();
            if ( response instanceof Status ) {
                if ( ((Status)response).getCode() != Code.SSH_FX_OK ) {
                    throw new IOException( new StatusException( (Status)response ) );
                }
            }
            else {
                throw new IOException( "Unexpected response: " + response.toString() );
            }
        }
        catch ( InterruptedException | ExecutionException e ) {
            throw new IOException( e.getMessage(), e );
        }
    }

    @Override
    public String toString() {
        return "{'handle':'" + handle.toString() + "'}";
    }
}
