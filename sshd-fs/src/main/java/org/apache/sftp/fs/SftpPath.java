package org.apache.sftp.fs;


import static org.apache.sftp.fs.SftpFileSystemProvider.PATH_SEPARATOR;


import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.AccessMode;
import java.nio.file.LinkOption;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.nio.file.ProviderMismatchException;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchEvent.Modifier;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


public class SftpPath implements Path {
    private static final String[] EMPTY_PARTS = new String[0];

    private boolean absolute;
    private SftpFileSystem fileSystem;
    private String[] parts;

    SftpPath( SftpFileSystem fileSystem, String path ) {
        this.fileSystem = fileSystem;
        this.absolute = false;

        if ( path == null || path.isEmpty() ) {
            this.parts = new String[0];
        }
        else {
            String[] parts = path.split( PATH_SEPARATOR + "+", 0 );
            if ( parts.length == 0 ) {
                this.absolute = true;
                this.parts = parts;
            }
            else if ( parts[0].isEmpty() ) {
                this.absolute = true;
                this.parts = Arrays.copyOfRange( parts, 1, parts.length );
            }
            else {
                this.parts = parts;
            }
        }
    }

    private SftpPath( SftpFileSystem fileSystem, boolean absolute, String... parts ) {
        this.fileSystem = fileSystem;
        this.absolute = absolute;
        this.parts = parts == null ? EMPTY_PARTS : parts;
    }

    @Override
    public int compareTo( Path other ) {
        if ( !getFileSystem().provider().equals( other.getFileSystem().provider() ) ) {
            throw new ClassCastException( "cannot compare paths from 2 different provider instances" );
        }
        return toString().compareTo( ((SftpPath)other).toString() );
    }

    @Override
    public boolean endsWith( Path path ) {
        if ( !getFileSystem().equals( path.getFileSystem() ) ) {
            return false;
        }
        if ( path.isAbsolute() && !isAbsolute() ) {
            return false;
        }

        int count = getNameCount();
        int otherCount = path.getNameCount();
        if ( otherCount > count ) {
            return false;
        }

        for ( count--, otherCount--; otherCount >= 0; count--, otherCount-- ) {
            if ( !path.getName( otherCount ).toString().equals( getName( count ).toString() ) ) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean endsWith( String path ) {
        return endsWith( new SftpPath( getFileSystem(), path ) );
    }

    @Override
    public boolean equals( Object other ) {
        if ( !(other instanceof SftpPath) ) {
            return false;
        }
        if ( !((SftpPath)other).getFileSystem()
                .equals( getFileSystem() ) ) {
            return false;
        }
        return toString().equals( other.toString() );
    }

    @Override
    public SftpPath getFileName() {
        if ( parts.length == 0 ) return null;
        return new SftpPath( getFileSystem(), false, getFileNameString() );
    }

    private String getFileNameString() {
        return parts[parts.length - 1];
    }

    @Override
    public SftpFileSystem getFileSystem() {
        return fileSystem;
    }

    @Override
    public SftpPath getName( int index ) {
        if ( index < 0 ) {
            throw new IllegalArgumentException();
        }
        if ( index >= parts.length ) {
            throw new IllegalArgumentException();
        }

        return new SftpPath( getFileSystem(),
                false, parts[index] );
    }

    @Override
    public int getNameCount() {
        return parts.length;
    }

    @Override
    public SftpPath getParent() {
        if ( parts.length == 0 && !isAbsolute() ) {
            return null;
        }
        if ( parts.length <= 1 ) {
            return new SftpPath( getFileSystem(), isAbsolute() );
        }
        return new SftpPath( getFileSystem(), isAbsolute(),
                Arrays.copyOfRange( parts, 0, parts.length - 1 ) );
    }

    @Override
    public SftpPath getRoot() {
        if ( isAbsolute() ) {
            return new SftpPath( getFileSystem(), true );
        }
        else {
            return null;
        }
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean isAbsolute() {
        return absolute;
    }

    @Override
    public Iterator<Path> iterator() {
        return new Iterator<Path>() {
            int index = 0;
            int count = getNameCount();

            public boolean hasNext() {
                return index < count;
            }

            public Path next() {
                return getName( index++ );
            }

            public void remove() {
                // path is immutable... dont want to allow changes
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public SftpPath normalize() {
        List<String> partsList = new ArrayList<String>();
        for ( String part : parts ) {
            if ( part.equals( "." ) ) {
                continue;
            }
            else if ( part.equals( ".." ) ) {
                int size = partsList.size();
                if ( size > 0 ) {
                    partsList.remove( size - 1 );
                }
            }
            else {
                partsList.add( part );
            }
        }
        return new SftpPath( getFileSystem(), isAbsolute(),
                partsList.toArray( new String[partsList.size()] ) );
    }

    @Override
    public WatchKey register( WatchService watcher, Kind<?>... events ) throws IOException {
        return register( watcher, events, new WatchEvent.Modifier[0] );
    }

    @Override
    public WatchKey register( WatchService watcher, Kind<?>[] events, Modifier... modifiers ) throws IOException {
        if ( watcher == null ) {
            throw new NullPointerException();
        }
        if ( !(watcher instanceof SftpFileSystemWatchService) ) {
            throw new ProviderMismatchException();
        }
        if ( !getFileSystem().provider().readAttributes( this, BasicFileAttributes.class ).isDirectory() ) {
            throw new NotDirectoryException( this.toString() );
        }
        getFileSystem().provider().checkAccess( this, AccessMode.READ );
        return ((SftpFileSystemWatchService)watcher).register( this, events, modifiers );
    }

    @Override
    public SftpPath relativize( Path other ) {
        if ( other == null ) {
            throw new NullPointerException();
        }
        if ( !(other instanceof SftpPath) ) {
            throw new ProviderMismatchException();
        }

        SftpPath unixOther = (SftpPath)other;
        if ( isAbsolute() && !unixOther.isAbsolute() ) {
            throw new IllegalArgumentException( "this and other must have same isAbsolute" );
        }

        if ( getNameCount() == 0 ) {
            return unixOther;
        }

        Path relative = null;
        Path remainingOther = null;
        Iterator<Path> otherIterator = unixOther.iterator();
        for ( Path part : this ) {
            if ( relative != null ) {
                relative = relative.resolve( ".." );
                continue;
            }

            if ( otherIterator.hasNext() ) {
                Path otherPart = otherIterator.next();
                if ( !part.equals( otherPart ) ) {
                    remainingOther = otherPart;
                    while ( otherIterator.hasNext() ) {
                        remainingOther = remainingOther.resolve(
                                otherIterator.next() );
                    }
                    relative = new SftpPath( getFileSystem(), ".." );
                }
            }
            else {
                relative = new SftpPath( getFileSystem(), ".." );
            }
        }

        if ( relative == null ) {
            while ( otherIterator.hasNext() ) {
                if ( remainingOther == null ) {
                    remainingOther = new SftpPath( getFileSystem(), "" );
                }
                else {
                    remainingOther = remainingOther.resolve(
                            otherIterator.next() );
                }
            }
            return remainingOther == null
                    ? new SftpPath( getFileSystem(), "" )
                    : (SftpPath)remainingOther;
        }
        return remainingOther == null
                ? (SftpPath)relative
                : (SftpPath)relative.resolve( remainingOther );
    }

    @Override
    public SftpPath resolve( Path other ) {
        if ( other.isAbsolute() ) {
            if ( other instanceof SftpPath ) {
                return (SftpPath)other;
            }
            else {
                return new SftpPath( getFileSystem(), other.toString() );
            }
        }
        else if ( other.getNameCount() == 0 ) {
            return this;
        }

        int count = other.getNameCount();
        String[] combined = new String[parts.length + count];
        System.arraycopy( parts, 0, combined, 0, parts.length );
        int index = parts.length;
        for ( Path otherPart : other ) {
            combined[index++] = otherPart.toString();
        }
        return new SftpPath( getFileSystem(), isAbsolute(), combined );
    }

    @Override
    public SftpPath resolve( String other ) {
        return resolve( new SftpPath( getFileSystem(), other ) );
    }

    @Override
    public SftpPath resolveSibling( Path other ) {
        return getParent().resolve( other );
    }

    @Override
    public SftpPath resolveSibling( String other ) {
        return resolveSibling( new SftpPath( getFileSystem(), other ) );
    }

    @Override
    public boolean startsWith( Path other ) {
        if ( !getFileSystem().equals( other.getFileSystem() ) ) {
            return false;
        }
        if ( (other.isAbsolute() && !isAbsolute()) ||
                (isAbsolute() && !other.isAbsolute()) ) {
            return false;
        }

        int count = getNameCount();
        int otherCount = other.getNameCount();
        if ( otherCount > count ) {
            return false;
        }

        for ( int i = 0; i < otherCount; i++ ) {
            if ( !other.getName( i ).toString().equals( getName( i ).toString() ) ) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean startsWith( String other ) {
        return startsWith( new SftpPath( getFileSystem(), other ) );
    }

    @Override
    public SftpPath subpath( int start, int end ) {
        String[] parts = new String[end - start];
        for ( int i = start; i < end; i++ ) {
            parts[i] = getName( i ).toString();
        }
        return new SftpPath( getFileSystem(), false, parts );
    }

    @Override
    public SftpPath toAbsolutePath() {
        if ( isAbsolute() ) {
            return this;
        }
        else {
            return getFileSystem().getDefaultDirectory().resolve( this );
        }
    }

    @Override
    public File toFile() {
        throw new UnsupportedOperationException( "path not from default provider" );
    }

    @Override
    public SftpPath toRealPath( LinkOption... options ) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for ( String part : parts ) {
            if ( builder.length() > 0 || isAbsolute() ) {
                builder.append( PATH_SEPARATOR );
            }
            builder.append( part );
        }

        return builder.toString();
    }

    @Override
    public URI toUri() {
        return getFileSystem().getUri().resolve( toAbsolutePath().toString() );
    }
}
