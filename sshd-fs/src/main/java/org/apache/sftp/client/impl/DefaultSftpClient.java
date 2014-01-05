package org.apache.sftp.client.impl;


import java.io.IOException;
import java.util.concurrent.ExecutionException;


import org.apache.sftp.client.SftpClient;
import org.apache.sftp.protocol.NegotiatedVersion;
import org.apache.sftp.protocol.PacketDataFactory;
import org.apache.sftp.protocol.client.RequestProcessor;
import org.apache.sftp.protocol.client.impl.DefaultRequestProcessor;
import org.apache.sftp.protocol.impl.DefaultPacketDataFactory;
import org.apache.sshd.ClientSession;
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
}
