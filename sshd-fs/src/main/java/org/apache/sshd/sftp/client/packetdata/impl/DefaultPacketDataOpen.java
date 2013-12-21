package org.apache.sshd.sftp.client.packetdata.impl;


import java.util.EnumSet;


import org.apache.sshd.sftp.PacketType;
import org.apache.sshd.sftp.client.packetdata.Handle;
import org.apache.sshd.sftp.client.packetdata.Open;
import org.apache.sshd.sftp.impl.MaskFactory;
import org.apache.sshd.sftp.impl.SftpFileAttributes;
import org.apache.sshd.sftp.impl.SftpProtocolBuffer;


public class DefaultPacketDataOpen
        extends AbstractPath<Open>
        implements Open {
    private SftpFileAttributes fileAttributes;
    private EnumSet<PFlag> pflags;

    protected DefaultPacketDataOpen() {
        fileAttributes = new SftpFileAttributes();
    }

    @Override
    public void appendToStringBuilder2( StringBuilder builder ) {
        builder.append( ",'pflags':" ).append( pflags )
                .append( "','fileAttributes':" ).append( fileAttributes );
    }

    @Override
    public Class<Handle> expectedResponseType() {
        return Handle.class;
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.SSH_FXP_OPEN;
    }

    @Override
    public SftpFileAttributes getFileAttributes() {
        return fileAttributes;
    }

    @Override
    public EnumSet<PFlag> getPFlags() {
        return pflags;
    }

    @Override
    public Open parseFrom2( SftpProtocolBuffer buffer ) {
        pflags = MaskFactory.fromMask( buffer.getInt(), PFlag.class );
        fileAttributes.parseFrom( buffer );
        return this;
    }

    @Override
    public Open setFileAttributes( SftpFileAttributes fileAttributes ) {
        if ( fileAttributes == null ) {
            this.fileAttributes = new SftpFileAttributes();
        }
        else {
            this.fileAttributes = fileAttributes;
        }
        return this;
    }

    @Override
    public Open setPFlags( EnumSet<PFlag> pflags ) {
        this.pflags = pflags;
        return this;
    }

    @Override
    public void writeTo2( SftpProtocolBuffer buffer ) {
        buffer.putInt( MaskFactory.toMask( pflags ) );
        fileAttributes.writeTo( buffer );
    }
}
