package org.apache.sftp.protocol.impl;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeNoException;
import static org.junit.Assume.assumeNotNull;
import static org.junit.Assume.assumeTrue;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;


import org.apache.sftp.protocol.StatusException;
import org.apache.sftp.protocol.UnexpectedPacketDataException;
import org.apache.sftp.protocol.client.CloseableHandle;
import org.apache.sftp.protocol.client.RequestProcessor;
import org.apache.sftp.protocol.client.impl.DefaultRequestProcessor;
import org.apache.sftp.protocol.impl.SftpFileAttributes.Permission;
import org.apache.sftp.protocol.packetdata.Attrs;
import org.apache.sftp.protocol.packetdata.FSetStat;
import org.apache.sftp.protocol.packetdata.FStat;
import org.apache.sftp.protocol.packetdata.Handle;
import org.apache.sftp.protocol.packetdata.LStat;
import org.apache.sftp.protocol.packetdata.MkDir;
import org.apache.sftp.protocol.packetdata.Name;
import org.apache.sftp.protocol.packetdata.Name.NameEntry;
import org.apache.sftp.protocol.packetdata.Open;
import org.apache.sftp.protocol.packetdata.Open.PFlag;
import org.apache.sftp.protocol.packetdata.OpenDir;
import org.apache.sftp.protocol.packetdata.Read;
import org.apache.sftp.protocol.packetdata.ReadDir;
import org.apache.sftp.protocol.packetdata.ReadLink;
import org.apache.sftp.protocol.packetdata.RealPath;
import org.apache.sftp.protocol.packetdata.Remove;
import org.apache.sftp.protocol.packetdata.Rename;
import org.apache.sftp.protocol.packetdata.RmDir;
import org.apache.sftp.protocol.packetdata.SetStat;
import org.apache.sftp.protocol.packetdata.Stat;
import org.apache.sftp.protocol.packetdata.Status;
import org.apache.sftp.protocol.packetdata.SymLink;
import org.apache.sftp.protocol.packetdata.Write;
import org.apache.sftp.protocol.packetdata.openssh.PosixRename;
import org.apache.sftp.test.TestUtil;
import org.apache.sshd.ClientSession;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DefaultRequestProcessorTest {
    private static Logger logger = LoggerFactory.getLogger( DefaultRequestProcessorTest.class );
    private static final Charset USASCII = Charset.forName( "US-ASCII" );
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
        testDirString = "org.apache.sftp.protocol.impl.DefaultRequestProcessorTest";
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
                            .setHandle( handle ) ).get();
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

    private byte[] readBytesFully( Path path ) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream( 1024 );
        try (InputStream inputStream = Files.newInputStream( path )) {
            int read = 0;
            byte[] bytes = new byte[1024];
            while ( (read = inputStream.read( bytes )) >= 0 ) {
                out.write( bytes, 0, read );
            }
        }
        return out.toByteArray();
    }

    private String readFully( Path path ) throws IOException {
        return readFully( path, UTF8 );
    }

    private String readFully( Path path, Charset charset ) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader( path, charset )) {
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ( (line = reader.readLine()) != null ) {
                builder.append( line );
            }
            return builder.toString();
        }
    }

    private static void recursiveDelete( Path path ) {
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

    private static void safeDelete( Path... paths ) {
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
        try (CloseableHandle handle = requestProcessor.requestCloseable( requestProcessor.newRequest( Open.class )
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
    public void testFSetStat() {
        String testFileString = "file.txt";
        Path testFile = testDir.resolve( testFileString );

        try {
            Files.createFile( testFile );
            assertTrue( Files.exists( testFile ) );
            BasicFileAttributes basicFileAttributes =
                    Files.readAttributes( testFile, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS );
            assertTrue( basicFileAttributes.lastAccessTime().toMillis() > 0 );
            assertTrue( basicFileAttributes.lastModifiedTime().toMillis() > 0 );

            String path = sshTempDirString + "/" + testDirString + "/" + testFileString;
            try (CloseableHandle handle = requestProcessor.requestCloseable(
                    requestProcessor.newRequest( Open.class )
                            .setPath( path ) )) {
                assertEquals( Status.Code.SSH_FX_OK,
                        requestProcessor.request( requestProcessor.newRequest( FSetStat.class )
                                .setHandle( handle )
                                .setFileAttributes( new SftpFileAttributes()
                                        .setTimes( FileTime.fromMillis( 0 ), FileTime.fromMillis( 0 ) ) ) )
                                .get()
                                .getCode() );
            }
            catch ( StatusException | UnexpectedPacketDataException e ) {
                logger.error( "fail:", e );
                fail( e.getMessage() );
            }

            basicFileAttributes =
                    Files.readAttributes( testFile, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS );
            assertTrue( basicFileAttributes.lastAccessTime().to( TimeUnit.SECONDS ) == 0 );
            assertTrue( basicFileAttributes.lastModifiedTime().to( TimeUnit.SECONDS ) == 0 );
        }
        catch ( IOException | InterruptedException | ExecutionException e ) {
            assumeNoException( e );
        }
    }

    @Test
    public void testFolderExists() {
        String path = sshTempDirString;
        try (CloseableHandle handle = requestProcessor.requestCloseable(
                requestProcessor.newRequest( Open.class ).setPath( path ) )) {
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
    public void testFStat() {
        try {
            BasicFileAttributes basicFileAttributes =
                    Files.readAttributes( testDir, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS );

            String path = sshTempDirString + "/" + testDirString;
            try (CloseableHandle handle = requestProcessor.requestCloseable(
                    requestProcessor.newRequest( OpenDir.class ).setPath( path ) )) {
                Attrs attrs = requestProcessor.request(
                        requestProcessor.newRequest( FStat.class ).setHandle( handle ) ).get();

                // The `atime' and `mtime' contain the access and modification
                // times
                // of the files, respectively. They are represented as seconds
                // from
                // Jan 1, 1970 in UTC.
                // So we can only check equality at the level of seconds...
                assertEquals( attrs.getFileAttributes().getLastAccessTime().to( TimeUnit.SECONDS ),
                        basicFileAttributes.lastAccessTime().to( TimeUnit.SECONDS ) );
                assertEquals( attrs.getFileAttributes().getLastModifiedTime().to( TimeUnit.SECONDS ),
                        basicFileAttributes.lastModifiedTime().to( TimeUnit.SECONDS ) );
            }
            catch ( StatusException | UnexpectedPacketDataException e ) {
                logger.debug( "Failed to read attributes: ", e );
                fail( "Failed to read attributes: " + e.getMessage() );
            }
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
            assertTrue( Files.exists( testFile1 ) );
            Files.createFile( testFile2 );
            assertTrue( Files.exists( testFile2 ) );

            String path = sshTempDirString + "/" + testDirString;
            try (CloseableHandle handle = requestProcessor.requestCloseable(
                    requestProcessor.newRequest( OpenDir.class ).setPath( path ) )) {
                assertNotNull( handle.getHandle() );
                for ( String filename : listDirectory( handle ) ) {
                    assertTrue( expectedFileNames.remove( filename ) );
                }
                assertTrue( expectedFileNames.isEmpty() );
            }
            catch ( StatusException e ) {
                logger.debug( "Unable to list directory:", e );
                fail( "Unable to list directory: " + e.getMessage() );
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
    public void testLStat() {
        try {
            BasicFileAttributes basicFileAttributes =
                    Files.readAttributes( testDir, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS );

            String path = sshTempDirString + "/" + testDirString;
            Attrs attrs = requestProcessor.request(
                    requestProcessor.newRequest( LStat.class ).setPath( path ) ).get();

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
    public void testMkDir() {
        String newDir = "newDir";
        Path path = Paths.get( osTempDirString, testDirString, newDir );
        String pathString = sshTempDirString + "/" + testDirString + "/" + newDir;
        assertFalse( Files.exists( path ) );

        try {
            assertEquals( Status.Code.SSH_FX_OK,
                    requestProcessor.request(
                            requestProcessor.newRequest( MkDir.class )
                                    .setPath( pathString ) ).get().getCode() );
        }
        catch ( InterruptedException | ExecutionException | IOException e ) {
            logger.error( "fail:", e );
            fail( e.getMessage() );
        }
        assertTrue( Files.exists( path ) );
    }

    @Test
    public void testNoSuchFile() {
        String path = sshTempDirString + "/" + testDirString + "/does_not_exist";
        try (CloseableHandle handle = requestProcessor.requestCloseable(
                requestProcessor.newRequest( Open.class ).setPath( path ) )) {
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
    public void testPosixRename() {
        String testFileString = "file.txt";
        String testFileNewString = "file_new.txt";
        Path testFile = testDir.resolve( testFileString );
        Path testFileNew = testDir.resolve( testFileNewString );

        String path = sshTempDirString + "/" + testDirString + "/" + testFileString;
        String newPath = sshTempDirString + "/" + testDirString + "/" + testFileNewString;
        try (CloseableHandle handle = requestProcessor.requestCloseable( requestProcessor.newRequest( Open.class )
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
        assertFalse( Files.exists( testFileNew ) );

        try {
            assertEquals( Status.Code.SSH_FX_OK,
                    requestProcessor.request( requestProcessor.newRequest( PosixRename.class )
                            .setPath( path )
                            .setTargetPath( newPath ) ).get().getCode() );
        }
        catch ( IOException | InterruptedException | ExecutionException e ) {
            logger.error( "fail:", e );
            fail( e.getMessage() );
        }
        assertFalse( Files.exists( testFile ) );
        assertTrue( Files.exists( testFileNew ) );
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
            try (CloseableHandle handle = requestProcessor.requestCloseable(
                    requestProcessor.newRequest( Open.class )
                            .setPath( path )
                            .setPFlags( EnumSet.of( PFlag.SSH_FXF_READ ) ) )) {
                assertNotNull( handle.getHandle() );

                assertEquals(
                        testFileContents,
                        new String( requestProcessor.request( requestProcessor.newRequest( Read.class )
                                .setHandle( handle )
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
    public void testReadLink() {
        String linkString = "link";
        Path linkPath = testDir.resolve( linkString );

        try {
            Files.createSymbolicLink( linkPath, testDir );
        }
        catch ( IOException e ) {
            assumeNoException( e );
        }

        String sshPath = sshTempDirString + "/" + testDirString;
        String sshLinkPath = sshTempDirString + "/" + testDirString + "/" + linkString;
        try {
            Name name = requestProcessor.request(
                    requestProcessor.newRequest( ReadLink.class )
                            .setPath( sshLinkPath ) ).get();
            assertEquals( sshPath, name.getNameEntries().get( 0 ).getFileName() );
        }
        catch ( IOException | InterruptedException | ExecutionException e ) {
            logger.error( "fail:", e );
            fail( e.getMessage() );
        }
    }

    @Test
    public void testRealPath() {
        String sshPath = sshTempDirString + "/" + testDirString;
        String sshLinkPath = sshTempDirString + "/" + testDirString + "/../" + testDirString;
        try {
            Name name = requestProcessor.request(
                    requestProcessor.newRequest( RealPath.class )
                            .setPath( sshLinkPath ) ).get();
            assertEquals( sshPath, name.getNameEntries().get( 0 ).getFileName() );
        }
        catch ( IOException | InterruptedException | ExecutionException e ) {
            logger.error( "fail:", e );
            fail( e.getMessage() );
        }
    }

    @Test
    public void testRemove() {
        String testFileString = "file.txt";
        Path testFile = testDir.resolve( testFileString );

        try {
            Files.createFile( testFile );
            assertTrue( Files.exists( testFile ) );

            String path = sshTempDirString + "/" + testDirString + "/" + testFileString;
            assertEquals( Status.Code.SSH_FX_OK,
                    requestProcessor.request(
                            requestProcessor.newRequest( Remove.class )
                                    .setPath( path ) ).get().getCode() );
            assertFalse( Files.exists( testFile ) );
        }
        catch ( IOException | InterruptedException | ExecutionException e ) {
            assumeNoException( e );
        }
    }

    @Test
    public void testRename() {
        String testFileString = "file.txt";
        String testFileNewString = "file_new.txt";
        Path testFile = testDir.resolve( testFileString );
        Path testFileNew = testDir.resolve( testFileNewString );

        String path = sshTempDirString + "/" + testDirString + "/" + testFileString;
        String newPath = sshTempDirString + "/" + testDirString + "/" + testFileNewString;
        try (CloseableHandle handle = requestProcessor.requestCloseable( requestProcessor.newRequest( Open.class )
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
        assertFalse( Files.exists( testFileNew ) );

        try {
            assertEquals( Status.Code.SSH_FX_OK,
                    requestProcessor.request( requestProcessor.newRequest( Rename.class )
                            .setPath( path )
                            .setTargetPath( newPath ) ).get().getCode() );
        }
        catch ( IOException | InterruptedException | ExecutionException e ) {
            logger.error( "fail:", e );
            fail( e.getMessage() );
        }
        assertFalse( Files.exists( testFile ) );
        assertTrue( Files.exists( testFileNew ) );
    }

    @Test
    public void testRmDir() {
        String newDirString = "newDir";
        Path path = Paths.get( osTempDirString, testDirString, newDirString );
        String pathString = sshTempDirString + "/" + testDirString + "/" + newDirString;

        try {
            Files.createDirectories( path );
        }
        catch ( IOException e ) {
            assumeNoException( e );
        }
        assertTrue( Files.exists( path ) );

        try {
            assertEquals( Status.Code.SSH_FX_OK,
                    requestProcessor.request(
                            requestProcessor.newRequest( RmDir.class )
                                    .setPath( pathString ) ).get().getCode() );
        }
        catch ( InterruptedException | ExecutionException | IOException e ) {
            logger.error( "fail:", e );
            fail( e.getMessage() );
        }
        assertFalse( Files.exists( path ) );
    }

    @Test
    public void testSetStat() {
        String testFileString = "file.txt";
        Path testFile = testDir.resolve( testFileString );

        try {
            Files.createFile( testFile );
            assertTrue( Files.exists( testFile ) );
            BasicFileAttributes basicFileAttributes =
                    Files.readAttributes( testFile, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS );
            assertTrue( basicFileAttributes.lastAccessTime().toMillis() > 0 );
            assertTrue( basicFileAttributes.lastModifiedTime().toMillis() > 0 );

            String path = sshTempDirString + "/" + testDirString + "/" + testFileString;
            assertEquals( Status.Code.SSH_FX_OK,
                    requestProcessor.request( requestProcessor.newRequest( SetStat.class )
                            .setPath( path )
                            .setFileAttributes( new SftpFileAttributes()
                                    .setTimes( FileTime.fromMillis( 0 ), FileTime.fromMillis( 0 ) ) ) )
                            .get()
                            .getCode() );

            basicFileAttributes =
                    Files.readAttributes( testFile, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS );
            assertTrue( basicFileAttributes.lastAccessTime().to( TimeUnit.SECONDS ) == 0 );
            assertTrue( basicFileAttributes.lastModifiedTime().to( TimeUnit.SECONDS ) == 0 );
        }
        catch ( IOException | InterruptedException | ExecutionException e ) {
            assumeNoException( e );
        }
    }

    @Test
    public void testStat() {
        try {
            BasicFileAttributes basicFileAttributes =
                    Files.readAttributes( testDir, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS );

            String path = sshTempDirString + "/" + testDirString;
            Attrs attrs = requestProcessor.request(
                    requestProcessor.newRequest( Stat.class ).setPath( path ) ).get();

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
    public void testSymLink() {
        String testFileString = "file.txt";
        String testLinkString = "linkToFile.txt";
        Path testFile = testDir.resolve( testFileString );
        Path testLink = testDir.resolve( testLinkString );

        String path = sshTempDirString + "/" + testDirString + "/" + testFileString;
        String linkPath = sshTempDirString + "/" + testDirString + "/" + testLinkString;
        try (CloseableHandle handle = requestProcessor.requestCloseable( requestProcessor.newRequest( Open.class )
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
        assertFalse( Files.exists( testLink ) );

        try {
            assertEquals( Status.Code.SSH_FX_OK,
                    requestProcessor.request( requestProcessor.newRequest( SymLink.class )
                            .setPath( path )
                            .setTargetPath( linkPath ) ).get().getCode() );
        }
        catch ( IOException | InterruptedException | ExecutionException e ) {
            logger.error( "fail:", e );
            fail( e.getMessage() );
        }
        assertTrue( Files.exists( testFile ) );
        assertTrue( Files.exists( testLink ) );

        if ( Files.isSymbolicLink( testLink ) ) {
            try {
                assertTrue( Files.isSameFile( testFile, testLink ) );
            }
            catch ( IOException e ) {
                logger.error( "fail:", e );
                fail( e.getMessage() );
            }
        }
        else {
            // check for cygwin sftpd
            try {
                byte[] bytes = readBytesFully( testLink );
                byte[] symlinkBytes = new byte[10];
                byte[] pathBytes = new byte[bytes.length - 10];
                System.arraycopy( bytes, 0, symlinkBytes, 0, symlinkBytes.length );
                System.arraycopy( bytes, 10, pathBytes, 0, pathBytes.length );
                assertTrue( Arrays.equals( symlinkBytes, "!<symlink>".getBytes( USASCII ) ) );

                // not sure how to decode the path part, so skip that test for
                // now
                // assertTrue( Arrays.equals( pathBytes, linkPath.getBytes(
                // UTF16 ) ) );
            }
            catch ( IOException e ) {
                logger.error( "fail:", e );
                fail( e.getMessage() );
            }
        }
    }

    @Test
    public void testWriteFile() {
        String testFileString = "file.txt";
        Path testFile = testDir.resolve( testFileString );

        byte[] bytes = testFileContents.getBytes( UTF8 );
        String path = sshTempDirString + "/" + testDirString + "/" + testFileString;
        try (CloseableHandle handle = requestProcessor.requestCloseable( requestProcessor.newRequest( Open.class )
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
                            .setHandle( handle )
                            .setOffset( 0 )
                            .setData( bytes ) ).get().getCode() );

            assertEquals( testFileContents, readFully( testFile ) );
        }
        catch ( StatusException | UnexpectedPacketDataException | IOException | InterruptedException | ExecutionException e ) {
            logger.error( "fail:", e );
            fail( e.getMessage() );
        }

        assertTrue( Files.exists( testFile ) );
    }
}
