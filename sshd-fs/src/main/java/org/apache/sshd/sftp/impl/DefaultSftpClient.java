package org.apache.sshd.sftp.impl;


import java.io.IOException;
import java.util.concurrent.ExecutionException;


import org.apache.sshd.ClientSession;
import org.apache.sshd.sftp.NegotiatedVersion;
import org.apache.sshd.sftp.PacketDataFactory;
import org.apache.sshd.sftp.PacketType;
import org.apache.sshd.sftp.RequestOrResponse;
import org.apache.sshd.sftp.RequestProcessor;
import org.apache.sshd.sftp.StatusException;
import org.apache.sshd.sftp.client.SftpClient;
import org.apache.sshd.sftp.client.packetdata.Handle;
import org.apache.sshd.sftp.client.packetdata.OpenDir;
import org.apache.sshd.sftp.client.packetdata.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DefaultSftpClient implements SftpClient {
    private static Logger logger = LoggerFactory.getLogger( DefaultSftpClient.class );
    private RequestProcessor requestProcessor;

    public DefaultSftpClient( ClientSession clientSession ) throws IOException, InterruptedException, ExecutionException {
        this( clientSession, new DefaultPacketDataFactory() );
    }

    public DefaultSftpClient( ClientSession clientSession, PacketDataFactory packetDataFactory ) throws IOException, InterruptedException, ExecutionException {
        this( new DefaultRequestProcessor( clientSession, packetDataFactory ) );
    }

    public DefaultSftpClient( RequestProcessor requestProcessor ) {
        this.requestProcessor = requestProcessor;
    }

    @Override
    public void close() throws IOException {
        logger.debug( "DefaultSftpClient closing" );
        if ( requestProcessor != null ) {
            requestProcessor.close();
        }
    }

    @Override
    public NegotiatedVersion negotiatedVersion() {
        return requestProcessor.negotiatedVersion();
    }

    @Override
    public DefaultHandle openDir( String path ) throws StatusException, IOException {
        try {
            RequestOrResponse<?> response = requestProcessor.request(
                    requestProcessor.newRequest( OpenDir.class ).setPath( path ) ).get();
            PacketType packetType = response.getPacketType();
            if ( packetType != null ) {
                switch ( packetType ) {
                    case SSH_FXP_HANDLE:
                        return new DefaultHandle( requestProcessor, (Handle)response );
                    case SSH_FXP_STATUS:
                        throw new StatusException( (Status)response );
                    default:
                        logger.error( "Server responded with unexpected packet: {}", response );
                        throw new UnsupportedOperationException( "Server responded with unexpected packet type " + response.getPacketType() );
                }
            }
            logger.error( "Server responded with unexpected packet: {}", response );
            throw new UnsupportedOperationException( "Server responded with unexpected packet type " + response.getPacketTypeByte() );
        }
        catch ( InterruptedException | ExecutionException e ) {
            throw new IOException( e.getMessage(), e );
        }
    }
}
