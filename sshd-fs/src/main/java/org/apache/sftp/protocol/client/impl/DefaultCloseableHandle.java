package org.apache.sftp.protocol.client.impl;


import java.io.IOException;
import java.util.concurrent.ExecutionException;


import org.apache.sftp.protocol.PacketType;
import org.apache.sftp.protocol.StatusException;
import org.apache.sftp.protocol.client.CloseableHandle;
import org.apache.sftp.protocol.client.RequestProcessor;
import org.apache.sftp.protocol.impl.SftpProtocolBuffer;
import org.apache.sftp.protocol.packetdata.Close;
import org.apache.sftp.protocol.packetdata.Handle;
import org.apache.sftp.protocol.packetdata.Status;
import org.apache.sftp.protocol.packetdata.Status.Code;


public class DefaultCloseableHandle implements CloseableHandle {
    private Handle handle;
    private RequestProcessor requestProcessor;

    DefaultCloseableHandle( RequestProcessor requestProcessor, Handle handle ) {
        this.requestProcessor = requestProcessor;
        this.handle = handle;
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
    public byte[] getHandle() {
        return handle.getHandle();
    }

    @Override
    public PacketType getPacketType() {
        return handle.getPacketType();
    }

    @Override
    public byte getPacketTypeByte() {
        return handle.getPacketTypeByte();
    }

    @Override
    public Handle parseFrom( SftpProtocolBuffer buffer ) {
        throw new UnsupportedOperationException( "Not allowed on CloseableHandle" );
    }

    @Override
    public Handle setHandle( byte[] handle ) {
        throw new UnsupportedOperationException( "Not allowed on CloseableHandle" );
    }

    @Override
    public Handle setHandle( Handle handle ) {
        throw new UnsupportedOperationException( "Not allowed on CloseableHandle" );
    }

    @Override
    public Handle setPacketTypeByte( byte packetDataTypeByte ) {
        throw new UnsupportedOperationException( "Not allowed on CloseableHandle" );
    }

    @Override
    public String toString() {
        return handle.toString();
    }

    @Override
    public void writeTo( SftpProtocolBuffer buffer ) {
        throw new UnsupportedOperationException( "Not allowed on CloseableHandle" );
    }
}
