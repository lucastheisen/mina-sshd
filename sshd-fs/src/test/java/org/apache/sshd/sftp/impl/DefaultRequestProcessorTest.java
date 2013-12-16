package org.apache.sshd.sftp.impl;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeNoException;
import static org.junit.Assume.assumeNotNull;
import static org.junit.Assume.assumeTrue;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


import org.apache.sshd.sftp.Handle;
import org.apache.sshd.sftp.RequestProcessor;
import org.apache.sshd.sftp.Response;
import org.apache.sshd.sftp.StatusException;
import org.apache.sshd.sftp.client.packetdata.OpenDir;
import org.apache.sshd.sftp.client.packetdata.ReadDir;
import org.apache.sshd.sftp.client.packetdata.Status;
import org.apache.sshd.test.TestUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DefaultRequestProcessorTest {
    private static Logger logger = LoggerFactory.getLogger( DefaultRequestProcessorTest.class );

    private String osTempDir;
    private RequestProcessor requestProcessor;
    private String sshTempDir;

    @After
    public void destroy() {
        if ( requestProcessor != null ) {
            try {
                requestProcessor.close();
            }
            catch ( IOException e ) {
                logger.debug( "failed to close requestProcessor {}", requestProcessor );
            }
        }
    }

    @Before
    public void initialize() {
        try {
            requestProcessor = new DefaultRequestProcessor( TestUtil.instance().newSession() );
            assumeNotNull( requestProcessor );
            osTempDir = TestUtil.instance().osTempDir();
            assumeNotNull( osTempDir );
            sshTempDir = TestUtil.instance().sshTempDir();
            assumeNotNull( sshTempDir );
        }
        catch ( IOException | InterruptedException | ExecutionException e ) {
            assumeNoException( e );
        }
        assumeTrue( requestProcessor.negotiatedVersion().version() >= 3 );
        logger.debug( "RequestProcessor negotiated version: {}", requestProcessor.negotiatedVersion() );
    }

    @Test
    public void testNoSuchFile() {
        String testFolder = "org.apache.ssh.sftp.impl.DefaultRequestProcessorTest";
        File tempDir = new File( osTempDir );
        File testDir = new File( tempDir, testFolder );
        if ( testDir.exists() ) {
            testDir.delete();
        }

        String path = sshTempDir + "/" + testFolder;
        try (Handle handle = openDir( path )) {
            fail( "path should not exist" );
        }
        catch ( StatusException e ) {
            assertEquals( Status.Code.SSH_FX_NO_SUCH_FILE, e.getStatus().getCode() );
        }
        catch ( IOException | InterruptedException | ExecutionException e ) {
            logger.error( "fail:", e );
            fail( e.getMessage() );
        }
    }

    @Test
    public void testFolderExists() {
        File tempDir = new File( osTempDir );
        assumeTrue( tempDir.exists() );

        String path = sshTempDir;
        try (Handle handle = openDir( path )) {
            assertNotNull( handle.getHandle() );
        }
        catch ( StatusException e ) {
            fail( "path should not exist" );
        }
        catch ( IOException | InterruptedException | ExecutionException e ) {
            logger.error( "fail:", e );
            fail( e.getMessage() );
        }
    }

    @Test
    public void testListDirectory() {
        String testFolder = "org.apache.ssh.sftp.impl.DefaultRequestProcessorTest";
        File tempDir = new File( osTempDir );
        File testDir = new File( tempDir, testFolder );
        File testFile1 = new File( testDir, "file1.txt" );
        File testFile2 = new File( testDir, "file2.txt" );
        if ( testDir.exists() ) {
            testDir.delete();
        }

        try {
            testDir.mkdir();
            testFile1.createNewFile();
            testFile2.createNewFile();
            assumeTrue( tempDir.exists() );

            String path = sshTempDir + "/" + testFolder;
            try (Handle handle = openDir( path )) {
                assertNotNull( handle.getHandle() );
                List<String> entries = listDirectory( handle );
            }
            catch ( StatusException e ) {
                fail( "path should not exist" );
            }
            catch ( IOException | InterruptedException | ExecutionException e ) {
                logger.error( "fail:", e );
                fail( e.getMessage() );
            }
        }
        catch ( IOException e ) {
            assumeNoException( e );
        }
        finally {
            testFile1.delete();
            testFile2.delete();
            testDir.delete();
        }
    }

    private List<String> listDirectory( Handle handle ) throws InterruptedException, ExecutionException, IOException {
        List<String> entries = new ArrayList<>();
        Response<?> response = requestProcessor.request(
                requestProcessor.newRequest( ReadDir.class )
                        .setHandle( handle.getHandle().getHandle() ) ).get();
        return entries;
    }

    private Handle openDir( String path ) throws StatusException, InterruptedException, ExecutionException, IOException {
        Response<?> response = requestProcessor.request(
                requestProcessor.newRequest( OpenDir.class ).setPath( path ) ).get();
        if ( response instanceof org.apache.sshd.sftp.client.packetdata.Handle ) {
            return new DefaultHandle( requestProcessor, (org.apache.sshd.sftp.client.packetdata.Handle)response );
        }
        else if ( response instanceof Status ) {
            throw new StatusException( (Status)response );
        }
        throw new UnsupportedOperationException( "Unknown response: " + response.toString() );
    }
}
