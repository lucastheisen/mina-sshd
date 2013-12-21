package org.apache.sshd.sftp.client.packetdata.impl;

import org.apache.sshd.sftp.PacketType;
import org.apache.sshd.sftp.client.packetdata.Attrs;
import org.apache.sshd.sftp.impl.SftpFileAttributes;
import org.apache.sshd.sftp.impl.SftpProtocolBuffer;

public class DefaultPacketDataAttrs extends AbstractPacketData<Attrs> implements Attrs {
    private SftpFileAttributes fileAttributes;
    
    protected DefaultPacketDataAttrs() {}

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
    public DefaultPacketDataAttrs parseFrom( SftpProtocolBuffer buffer ) {
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
