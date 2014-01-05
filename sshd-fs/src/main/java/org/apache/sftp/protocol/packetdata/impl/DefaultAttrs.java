package org.apache.sftp.protocol.packetdata.impl;

import org.apache.sftp.protocol.PacketType;
import org.apache.sftp.protocol.impl.SftpFileAttributes;
import org.apache.sftp.protocol.impl.SftpProtocolBuffer;
import org.apache.sftp.protocol.packetdata.Attrs;

public class DefaultAttrs extends AbstractPacketData<Attrs> implements Attrs {
    private SftpFileAttributes fileAttributes;
    
    @Override
    public void appendToStringBuilder( StringBuilder builder ) {
        builder.append( ",'fileAttributes':" ).append( fileAttributes );
    }

    @Override
    public SftpFileAttributes getFileAttributes() {
        return fileAttributes;
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.SSH_FXP_ATTRS;
    }

    @Override
    public DefaultAttrs parseFrom( SftpProtocolBuffer buffer ) {
        fileAttributes = new SftpFileAttributes().parseFrom( buffer );
        return this;
    }

    @Override
    public Attrs setFileAttributes( SftpFileAttributes fileAttributes ) {
        this.fileAttributes = fileAttributes;
        return this;
    }

    @Override
    public void writeTo( SftpProtocolBuffer buffer ) {
        fileAttributes.writeTo( buffer );
    }
}
