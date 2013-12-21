package org.apache.sshd.sftp.client.packetdata.impl;


import java.util.ArrayList;
import java.util.List;


import org.apache.sshd.sftp.PacketType;
import org.apache.sshd.sftp.client.packetdata.Name;
import org.apache.sshd.sftp.impl.SftpFileAttributes;
import org.apache.sshd.sftp.impl.SftpProtocolBuffer;


public class DefaultPacketDataName
        extends AbstractPacketData<Name>
        implements Name {
    private List<NameEntry> nameEntries;

    protected DefaultPacketDataName() {
        this.nameEntries = new ArrayList<>();
    }

    @Override
    public void addNameEntry( String fileName, String longName, SftpFileAttributes fileAttributes ) {
        nameEntries.add( new NameEntryImpl()
                .setFileName( fileName )
                .setLongName( longName )
                .setFileAttributes( fileAttributes ) );
    }

    @Override
    public void appendToStringBuilder( StringBuilder builder ) {
        builder.append( ",'count':" ).append( getCount() );
    }

    @Override
    public int getCount() {
        return nameEntries.size();
    }

    @Override
    public List<NameEntry> getNameEntries() {
        return nameEntries;
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.SSH_FXP_NAME;
    }

    @Override
    public Name setNameEntries( List<NameEntry> nameEntries ) {
        this.nameEntries = nameEntries;
        return this;
    }

    @Override
    public Name parseFrom( SftpProtocolBuffer buffer ) {
        int count = buffer.getInt();
        for ( int i = 0; i < count; i++ ) {
            nameEntries.add( new NameEntryImpl()
                    .setFileName( buffer.getString() )
                    .setLongName( buffer.getString() )
                    .setFileAttributes(
                            new SftpFileAttributes().parseFrom( buffer ) ) );
        }
        return this;
    }

    @Override
    public void writeTo( SftpProtocolBuffer buffer ) {
        buffer.putInt( getCount() );
        for ( NameEntry nameEntry : nameEntries ) {
            buffer.putString( nameEntry.getFileName() );
            buffer.putString( nameEntry.getLongName() );
            nameEntry.getFileAttributes().writeTo( buffer );
        }
    }

    private static class NameEntryImpl implements NameEntry {
        private String fileName;
        private String longName;
        private SftpFileAttributes fileAttributes;

        @Override
        public SftpFileAttributes getFileAttributes() {
            return fileAttributes;
        }

        @Override
        public String getFileName() {
            return fileName;
        }

        @Override
        public String getLongName() {
            return longName;
        }

        @Override
        public NameEntry setFileAttributes( SftpFileAttributes fileAttributes ) {
            this.fileAttributes = fileAttributes;
            return this;
        }

        @Override
        public NameEntry setFileName( String fileName ) {
            this.fileName = fileName;
            return this;
        }

        @Override
        public NameEntry setLongName( String longName ) {
            this.longName = longName;
            return this;
        }
    }
}
