package org.apache.sftp.protocol.packetdata;


import java.util.List;


import org.apache.sftp.protocol.Response;
import org.apache.sftp.protocol.impl.SftpFileAttributes;


public interface Name extends Response<Name> {
    public void addNameEntry( String fileName, String longName, SftpFileAttributes fileAttributes );

    public int getCount();

    public List<NameEntry> getNameEntries();

    public Name setNameEntries( List<NameEntry> nameEntries );

    public interface NameEntry {
        public SftpFileAttributes getFileAttributes();

        public String getFileName();

        public String getLongName();

        public NameEntry setFileAttributes( SftpFileAttributes fileAttributes );

        public NameEntry setFileName( String fileName );

        public NameEntry setLongName( String longName );
    }
}
