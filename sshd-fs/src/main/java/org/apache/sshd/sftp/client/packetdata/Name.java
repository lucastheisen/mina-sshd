package org.apache.sshd.sftp.client.packetdata;


import java.util.List;


import org.apache.sshd.sftp.Response;
import org.apache.sshd.sftp.impl.SftpFileAttributes;


public interface Name extends Response<Name> {
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
