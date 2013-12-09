package org.apache.sshd.fs;


import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.EnumSet;


import org.apache.mina.core.future.CloseFuture;
import org.apache.sshd.ClientChannel;
import org.apache.sshd.ClientSession;
import org.apache.sshd.SshClient;
import org.apache.sshd.client.SftpClient;
import org.apache.sshd.client.future.ConnectFuture;
import org.apache.sshd.common.future.SshFutureListener;
import org.apache.sshd.common.util.NoCloseInputStream;
import org.apache.sshd.common.util.NoCloseOutputStream;
import org.apache.sshd.util.Utils;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class GettingStartedTest {
    private static Logger logger = LoggerFactory.getLogger( GettingStartedTest.class );

    @Test
    public void testCommand() {
        final String host = "localhost";
        final int port = 22;
        final String login = "ltheisen";
        final String password = "W4yTLoonky";
        final String expected = "hello world";

        SshClient client = SshClient.setUpDefaultClient();
        ClientSession session = null;
        try {
            client.start();
            session = client.connect( host, port ).await().getSession();
            session.authPassword( login, password );
            ClientChannel channel = session.createChannel( "exec", "echo " + expected );
            channel.setIn( new NoCloseInputStream( System.in ) );
            channel.setErr( new NoCloseOutputStream( System.err ) );
            channel.open().await();
            InputStream sshOut = channel.getInvertedOut();
            byte[] buffer = new byte[1024];
            int bytesRead = sshOut.read( buffer, 0, 1024 );
            Assert.assertEquals( bytesRead, 12 );
            Assert.assertEquals( expected + "\n", 
                    new String( buffer, 0, bytesRead, Charset.forName( "UTF-8" ) ) );
            Assert.assertEquals( ClientChannel.CLOSED, 
                    ClientChannel.CLOSED & channel.waitFor( ClientChannel.CLOSED, 0 ) );
            session.close( false );
        }
        catch ( InterruptedException e ) {
            logger.error( "Run command interrupted: {}", e.getMessage() );
            logger.debug( "Run command interrupted: ", e );
        }
        catch ( IOException e ) {
            logger.error( "Failed to run command: {}", e.getMessage() );
            logger.debug( "Failed to run command: ", e );
        }
        finally {
            if ( session != null ) {
                session.close( true );
            }
            client.stop();
        }
    }

    @Test
    public void testClient() throws Exception {
        final String hostname = "localhost";
        final int port = 22;
        final String username = "ltheisen";
        final String password = "W4yTLoonky";
        final File targetSlashSftp = new File( "C:/cygwin64/home/ltheisen/target/sftp" );

        SshClient client = SshClient.setUpDefaultClient();
        client.start();
        ClientSession session = client.connect( hostname, port ).await().getSession();
        session.authPassword( username, password ).await();

        deleteRecursive( targetSlashSftp );
        targetSlashSftp.mkdirs();

        SftpClient sftp = session.createSftpClient();

        sftp.mkdir( "target/sftp/client" );

        SftpClient.Handle h = sftp.open( "target/sftp/client/test.txt", EnumSet.of( SftpClient.OpenMode.Write ) );
        byte[] d = "0123456789\n".getBytes();
        sftp.write( h, 0, d, 0, d.length );
        sftp.write( h, d.length, d, 0, d.length );

        SftpClient.Attributes attrs = sftp.stat( h );
        Assert.assertNotNull( attrs );

        sftp.close( h );

        h = sftp.openDir( "target/sftp/client" );
        SftpClient.DirEntry[] dir = sftp.readDir( h );
        assertNotNull( dir );
        assertEquals( 1, dir.length );
        assertNull( sftp.readDir( h ) );
        sftp.close( h );

        sftp.remove( "target/sftp/client/test.txt" );

        OutputStream os = sftp.write( "target/sftp/client/test.txt" );
        os.write( new byte[1024 * 128] );
        os.close();

        InputStream is = sftp.read( "target/sftp/client/test.txt" );
        is.read( new byte[1024 * 128] );
        int i = is.read();
        is.close();

        int nb = 0;
        for ( SftpClient.DirEntry entry : sftp.readDir( "target/sftp/client" ) ) {
            nb++;
        }
        assertEquals( 1, nb );

        sftp.remove( "target/sftp/client/test.txt" );

        sftp.rmdir( "target/sftp/client/" );

        sftp.close();

        client.stop();
    }

    public static void deleteRecursive( File file ) {
        if ( file != null ) {
            if ( file.isDirectory() ) {
                File[] children = file.listFiles();
                if ( children != null ) {
                    for ( File child : children ) {
                        deleteRecursive( child );
                    }
                }
            }
            file.delete();
        }
    }

}
