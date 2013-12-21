package org.apache.sshd.sftp.impl;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeNoException;
import static org.junit.Assume.assumeNotNull;
import static org.junit.Assume.assumeTrue;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;


import org.apache.sshd.ClientSession;
import org.apache.sshd.sftp.Handle;
import org.apache.sshd.sftp.RequestProcessor;
import org.apache.sshd.sftp.Response;
import org.apache.sshd.sftp.StatusException;
import org.apache.sshd.sftp.UnexpectedPacketDataException;
import org.apache.sshd.sftp.client.packetdata.Attrs;
import org.apache.sshd.sftp.client.packetdata.Name;
import org.apache.sshd.sftp.client.packetdata.Name.NameEntry;
import org.apache.sshd.sftp.client.packetdata.Open;
import org.apache.sshd.sftp.client.packetdata.Open.PFlag;
import org.apache.sshd.sftp.client.packetdata.OpenDir;
import org.apache.sshd.sftp.client.packetdata.Read;
import org.apache.sshd.sftp.client.packetdata.ReadDir;
import org.apache.sshd.sftp.client.packetdata.Stat;
import org.apache.sshd.sftp.client.packetdata.Status;
import org.apache.sshd.sftp.client.packetdata.Write;
import org.apache.sshd.sftp.impl.SftpFileAttributes.Permission;
import org.apache.sshd.test.TestUtil;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DefaultRequestProcessorTest {
    private static Logger logger = LoggerFactory.getLogger( DefaultRequestProcessorTest.class );
    private static final Charset UTF8 = Charset.forName( "UTF-8" );
    private static String osTempDirString;
    private static String sshTempDirString;
    private static Path testDir;
    private static String testDirString;
    private static String testFileContents;
    private static ClientSession session;

    private RequestProcessor requestProcessor;

    @AfterClass
    public static void afterClass() {
        session.close( true );
    }

    @BeforeClass
    public static void beforeClass() throws InterruptedException, IOException {
        session = TestUtil.instance().newSession();
        osTempDirString = TestUtil.instance().osTempDir();
        assumeNotNull( osTempDirString );
        sshTempDirString = TestUtil.instance().sshTempDir();
        assumeNotNull( sshTempDirString );
        testDirString = "org.apache.ssh.sftp.impl.DefaultRequestProcessorTest";
        testDir = Paths.get( osTempDirString, testDirString );
        testFileContents = "this rocks in UTF-8";
    }

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

        recursiveDelete( testDir );
    }

    private List<String> listDirectory( Handle handle ) throws InterruptedException, ExecutionException, IOException, StatusException, UnexpectedPacketDataException {
        List<String> entries = new ArrayList<>();
        try {
            Name response = requestProcessor.request(
                    requestProcessor.newRequest( ReadDir.class )
                            .setHandle( handle.getHandle().getHandle() ) ).get();
            for ( NameEntry entry : ((Name)response).getNameEntries() ) {
                entries.add( entry.getFileName() );
            }
        }
        catch ( ExecutionException e ) {
            Throwable cause = e.getCause();
            if ( cause == null ) {
                throw e;
            }
            else if ( cause instanceof StatusException ) {
                throw (StatusException)cause;
            }
            else if ( cause instanceof UnexpectedPacketDataException ) {
                throw (UnexpectedPacketDataException)cause;
            }
            throw e;
        }
        return entries;
    }

    private Handle open( Open request ) throws StatusException, InterruptedException, ExecutionException, IOException, UnexpectedPacketDataException {
        try {
            return new DefaultHandle( requestProcessor,
                    requestProcessor.request( request ).get() );
        }
        catch ( ExecutionException e ) {
            Throwable cause = e.getCause();
            if ( cause == null ) {
                throw e;
            }
            else if ( cause instanceof StatusException ) {
                throw (StatusException)cause;
            }
            else if ( cause instanceof UnexpectedPacketDataException ) {
                throw (UnexpectedPacketDataException)cause;
            }
            throw e;
        }
    }

    private Handle openDir( String path ) throws StatusException, InterruptedException, ExecutionException, IOException, UnexpectedPacketDataException {
        try {
            return new DefaultHandle( requestProcessor, requestProcessor.request(
                    requestProcessor.newRequest( OpenDir.class ).setPath( path ) ).get() );
        }
        catch ( ExecutionException e ) {
            Throwable cause = e.getCause();
            if ( cause == null ) {
                throw e;
            }
            else if ( cause instanceof StatusException ) {
                throw (StatusException)cause;
            }
            else if ( cause instanceof UnexpectedPacketDataException ) {
                throw (UnexpectedPacketDataException)cause;
            }
            throw e;
        }
    }

    @Before
    public void prepare() {
        try {
            requestProcessor = new DefaultRequestProcessor( session );
            assumeNotNull( requestProcessor );

            recursiveDelete( testDir );
            Files.createDirectories( testDir );
        }
        catch ( IOException | InterruptedException | ExecutionException e ) {
            assumeNoException( e );
        }
        assumeTrue( requestProcessor.negotiatedVersion().version() >= 3 );
        logger.debug( "RequestProcessor negotiated version: {}", requestProcessor.negotiatedVersion() );

    }

    private String readFully( BufferedReader reader ) throws IOException {
        StringBuilder builder = new StringBuilder();
        String line = null;
        while ( (line = reader.readLine()) != null ) {
            builder.append( line );
        }
        return builder.toString();
    }

    private void recursiveDelete( Path path ) {
        if ( Files.exists( path ) ) {
            if ( Files.isDirectory( path ) ) {
                try (DirectoryStream<Path> directory = Files.newDirectoryStream( path )) {
                    for ( Path entry : directory ) {
                        recursiveDelete( entry );
                    }
                }
                catch ( IOException e ) {
                    logger.debug( "Failed to read directory:", e.getMessage() );
                }
                safeDelete( path );
            }
            else {
                safeDelete( path );
            }
        }
    }

    private void safeDelete( Path... paths ) {
        for ( Path path : paths ) {
            try {
                Files.deleteIfExists( path );
            }
            catch ( IOException e ) {
                logger.debug( "Failed to delete path {}: ", path, e.getMessage() );
            }
        }
    }

    @Test
    public void testCreateFile() {
        String testFileString = "file.txt";
        Path testFile = testDir.resolve( testFileString );

        String path = sshTempDirString + "/" + testDirString + "/" + testFileString;
        try (Handle handle = open( requestProcessor.newRequest( Open.class )
                .setPath( path )
                .setPFlags( EnumSet.of( PFlag.SSH_FXF_CREAT ) )
                .setFileAttributes( new SftpFileAttributes()
                        .setPermissions( EnumSet.of( Permission.S_IRUSR ) )
                        .setSize( 10 )
                        .setTimes( FileTime.fromMillis( 0 ), FileTime.fromMillis( 0 ) )
                        .setUidGid( 100, 100 )
                ) )) {
            assertNotNull( handle.getHandle() );
        }
        catch ( StatusException | UnexpectedPacketDataException | IOException | InterruptedException | ExecutionException e ) {
            logger.error( "fail:", e );
            fail( e.getMessage() );
        }

        assertTrue( Files.exists( testFile ) );
    }

    @Test
    public void testFolderExists() {
        String path = sshTempDirString;
        try (Handle handle = openDir( path )) {
            assertNotNull( handle.getHandle() );
        }
        catch ( StatusException e ) {
            fail( "path should not exist" );
        }
        catch ( UnexpectedPacketDataException | IOException | InterruptedException | ExecutionException e ) {
            logger.error( "fail:", e );
            fail( e.getMessage() );
        }
    }

    @Test
    public void testListDirectory() {
        Path testFile1 = testDir.resolve( "file1.txt" );
        Path testFile2 = testDir.resolve( "file2.txt" );

        Set<String> expectedFileNames = new HashSet<String>();
        expectedFileNames.add( "." );
        expectedFileNames.add( ".." );
        expectedFileNames.add( testFile1.getFileName().toString() );
        expectedFileNames.add( testFile2.getFileName().toString() );

        try {
            Files.createFile( testFile1 );
            Files.createFile( testFile2 );

            String path = sshTempDirString + "/" + testDirString;
            try (Handle handle = openDir( path )) {
                assertNotNull( handle.getHandle() );
                for ( String filename : listDirectory( handle ) ) {
                    assertTrue( expectedFileNames.remove( filename ) );
                }
                assertTrue( expectedFileNames.isEmpty() );
            }
            catch ( StatusException e ) {
                fail( "path should not exist" );
            }
            catch ( UnexpectedPacketDataException | IOException | InterruptedException | ExecutionException e ) {
                logger.error( "fail:", e );
                fail( e.getMessage() );
            }
        }
        catch ( IOException e ) {
            assumeNoException( e );
        }
    }

    @Test
    public void testNoSuchFile() {
        String path = sshTempDirString + "/" + testDirString + "/does_not_exist";
        try (Handle handle = openDir( path )) {
            fail( "path should not exist" );
        }
        catch ( StatusException e ) {
            assertEquals( Status.Code.SSH_FX_NO_SUCH_FILE, e.getStatus().getCode() );
        }
        catch ( UnexpectedPacketDataException | IOException | InterruptedException | ExecutionException e ) {
            logger.error( "fail:", e );
            fail( e.getMessage() );
        }
    }

    @Test
    public void testReadFile() {
        String testFileString = "file.txt";
        Path testFile = testDir.resolve( testFileString );

        try {
            try (BufferedWriter writer = Files.newBufferedWriter( testFile, UTF8,
                    StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE )) {
                writer.write( testFileContents );
            }
            assertTrue( Files.exists( testFile ) );

            String path = sshTempDirString + "/" + testDirString + "/" + testFileString;
            try (Handle handle = open( requestProcessor.newRequest( Open.class )
                    .setPath( path )
                    .setPFlags( EnumSet.of( PFlag.SSH_FXF_READ ) ) )) {
                assertNotNull( handle.getHandle() );

                assertEquals(
                        testFileContents,
                        new String( requestProcessor.request( requestProcessor.newRequest( Read.class )
                                .setHandle( handle.getHandle().getHandle() )
                                .setOffset( 0 )
                                .setLength( testFileContents.length() ) ).get().getData(), UTF8 ) );
            }
            catch ( StatusException | UnexpectedPacketDataException | IOException | InterruptedException | ExecutionException e ) {
                logger.error( "fail:", e );
                fail( e.getMessage() );
            }

            assertTrue( Files.exists( testFile ) );
        }
        catch ( IOException e ) {
            assumeNoException( e );
        }
    }

    @Test
    public void testStat() {
        try {
            BasicFileAttributes basicFileAttributes =
                    Files.readAttributes( testDir, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS );

            String path = sshTempDirString + "/" + testDirString;
            Response<?> response = requestProcessor.request(
                    requestProcessor.newRequest( Stat.class ).setPath( path ) ).get();
            assertTrue( response instanceof Attrs );
            Attrs attrs = (Attrs)response;

            // The `atime' and `mtime' contain the access and modification times
            // of the files, respectively. They are represented as seconds from
            // Jan 1, 1970 in UTC.
            // So we can only check equality at the level of seconds...
            assertEquals( attrs.getFileAttributes().getLastAccessTime().to( TimeUnit.SECONDS ),
                    basicFileAttributes.lastAccessTime().to( TimeUnit.SECONDS ) );
            assertEquals( attrs.getFileAttributes().getLastModifiedTime().to( TimeUnit.SECONDS ),
                    basicFileAttributes.lastModifiedTime().to( TimeUnit.SECONDS ) );
        }
        catch ( IOException e ) {
            assumeNoException( e );
        }
        catch ( InterruptedException e ) {
            assumeNoException( e );
        }
        catch ( ExecutionException e ) {
            assumeNoException( e );
        }
    }

    @Test
    public void testWriteFile() {
        String testFileString = "file.txt";
        Path testFile = testDir.resolve( testFileString );

        byte[] bytes = testFileContents.getBytes( UTF8 );
        String path = sshTempDirString + "/" + testDirString + "/" + testFileString;
        try (Handle handle = open( requestProcessor.newRequest( Open.class )
                .setPath( path )
                .setPFlags( EnumSet.of( PFlag.SSH_FXF_CREAT, PFlag.SSH_FXF_WRITE ) )
                .setFileAttributes( new SftpFileAttributes()
                        .setPermissions( EnumSet.of( Permission.S_IRUSR ) )
                        .setSize( bytes.length )
                        .setTimes( FileTime.fromMillis( 0 ), FileTime.fromMillis( 0 ) )
                        .setUidGid( 100, 100 )
                ) )) {
            assertNotNull( handle.getHandle() );
            assertTrue( Files.exists( testFile ) );

            assertEquals(
                    Status.Code.SSH_FX_OK,
                    requestProcessor.request( requestProcessor.newRequest( Write.class )
                            .setHandle( handle.getHandle().getHandle() )
                            .setOffset( 0 )
                            .setData( bytes ) ).get().getCode() );

            try (BufferedReader reader = Files.newBufferedReader( testFile, UTF8 )) {
                assertEquals( testFileContents, readFully( reader ) );
            }
        }
        catch ( StatusException | UnexpectedPacketDataException | IOException | InterruptedException | ExecutionException e ) {
            logger.error( "fail:", e );
            fail( e.getMessage() );
        }

        assertTrue( Files.exists( testFile ) );
    }
}
