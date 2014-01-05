package org.apache.sftp.protocol.packetdata.impl;


import java.util.ArrayList;
import java.util.List;


import org.apache.sftp.protocol.PacketType;
import org.apache.sftp.protocol.impl.SftpFileAttributes;
import org.apache.sftp.protocol.impl.SftpProtocolBuffer;
import org.apache.sftp.protocol.packetdata.Name;


public class DefaultName
        extends AbstractPacketData<Name>
        implements Name {
    private List<NameEntry> nameEntries;

    public DefaultName() {
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
