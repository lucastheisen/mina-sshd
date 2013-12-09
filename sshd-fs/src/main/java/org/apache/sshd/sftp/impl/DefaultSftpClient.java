package org.apache.sshd.sftp.impl;


import java.io.IOException;
import java.util.concurrent.ExecutionException;


import org.apache.sshd.ClientSession;
import org.apache.sshd.sftp.PacketDataFactory;
import org.apache.sshd.sftp.RequestProcessor;
import org.apache.sshd.sftp.client.SftpClient;


public class DefaultSftpClient implements SftpClient {
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
        if ( requestProcessor != null ) {
            requestProcessor.close();
        }
    }
    
    @Override
    public int negotiatedVersion() {
        return requestProcessor.negotiatedVersion();
    }
}
