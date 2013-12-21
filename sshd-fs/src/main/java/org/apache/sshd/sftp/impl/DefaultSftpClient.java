package org.apache.sshd.sftp.impl;


import java.io.IOException;
import java.util.concurrent.ExecutionException;


import org.apache.sshd.ClientSession;
import org.apache.sshd.sftp.NegotiatedVersion;
import org.apache.sshd.sftp.PacketDataFactory;
import org.apache.sshd.sftp.RequestProcessor;
import org.apache.sshd.sftp.StatusException;
import org.apache.sshd.sftp.client.SftpClient;
import org.apache.sshd.sftp.client.packetdata.impl.DefaultPacketDataFactory;
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
    public org.apache.sshd.sftp.Handle openDir( String path ) throws StatusException, IOException {
        // TODO Auto-generated method stub
        return null;
    }
}
