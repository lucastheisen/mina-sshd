package org.apache.sshd.sftp.impl;


import java.util.ArrayList;
import java.util.List;


import org.apache.sshd.sftp.PacketType;
import org.apache.sshd.sftp.client.packetdata.Name;


public class DefaultPacketDataName
        extends AbstractRequestOrResponse<Name>
        implements Name {
    private List<NameEntry> nameEntries;

    public DefaultPacketDataName() {
        nameEntries = new ArrayList<>();
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
    public Name parseRequestFrom( SftpProtocolBuffer buffer ) {
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
    public void writeRequestTo( SftpProtocolBuffer buffer ) {
        buffer.putInt( getCount() );
        for ( NameEntry nameEntry : nameEntries ) {
            buffer.putString( nameEntry.getFileName() );
            buffer.putString( nameEntry.getLongName() );
            nameEntry.getFileAttributes().writeTo( buffer );
        }
    }

    private class NameEntryImpl implements NameEntry {
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
