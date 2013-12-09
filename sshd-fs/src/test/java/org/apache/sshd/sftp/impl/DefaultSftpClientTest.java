package org.apache.sshd.sftp.impl;


import java.io.IOException;
import java.util.concurrent.ExecutionException;


import org.apache.sshd.ClientSession;
import org.apache.sshd.SshClient;
import org.apache.sshd.sftp.client.SftpClient;
import org.apache.sshd.sftp.impl.DefaultSftpClient;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DefaultSftpClientTest {
    private static Logger logger = LoggerFactory.getLogger( DefaultSftpClientTest.class );

    @Test
    public void testConnection() throws InterruptedException, IOException, ExecutionException {
        final String hostname = "localhost";
        final int port = 22;
        final String username = "ltheisen";
        final String password = "W4yTLoonky";

        SshClient client = SshClient.setUpDefaultClient();
        client.start();
        ClientSession session = client.connect( hostname, port ).await().getSession();
        session.authPassword( username, password ).await();

        try (SftpClient sftpClient = new DefaultSftpClient( session )) {
            logger.debug( "SftpClient negotiated version: {}", sftpClient.negotiatedVersion() );
        }
    }
}
