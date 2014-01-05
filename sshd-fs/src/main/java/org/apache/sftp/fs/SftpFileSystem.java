package org.apache.sftp.fs;


import java.io.IOException;
import java.net.URI;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.WatchService;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


import org.apache.sftp.protocol.client.RequestProcessor;
import org.apache.sshd.ClientSession;
import org.apache.sshd.SshClient;


public class SftpFileSystem extends FileSystem {
    private static final Set<String> supportedFileAttributeViews;

    static {
        supportedFileAttributeViews = new HashSet<String>();
        supportedFileAttributeViews.add( "basic" );
    }

    private SftpPath defaultDirectory;
    private Map<String, ?> environment;
    private SftpFileSystemProvider provider;
    private RequestProcessor requestProcessor;
    private SftpPath rootDirectory;
    private URI uri;
    
    SftpFileSystem( SftpFileSystemProvider provider, URI uri, Map<String, ?> environment ) {
        this.provider = provider;
        this.uri = uri;
        
        //TODO: build requestProcessor
//        try {
//            SshClient client = SshClient.setUpDefaultClient();
//            client.start();
//            ClientSession session = client.connect( hostname, port ).await().getSession();
//            session.authPassword( username, password ).await();
//            return session;
//        }
    }

    @Override
    public SftpFileSystemProvider provider() {
        return provider;
    }

    @Override
    public void close() throws IOException {
        requestProcessor.close();
        provider().removeFileSystem( this );
    }

    @Override
    public boolean isOpen() {
        // TODO: updated requestProcessor so that it reopens if closed
        return true;
    }

    @Override
    public boolean isReadOnly() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public String getSeparator() {
        return SftpFileSystemProvider.PATH_SEPARATOR_STRING;
    }

    @Override
    public Iterable<Path> getRootDirectories() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Iterable<FileStore> getFileStores() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<String> supportedFileAttributeViews() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Path getPath( String first, String... more ) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PathMatcher getPathMatcher( String syntaxAndPattern ) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UserPrincipalLookupService getUserPrincipalLookupService() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public WatchService newWatchService() throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    SftpPath getDefaultDirectory() {
        return defaultDirectory;
    }

    URI getUri() {
        return uri;
    }
}
