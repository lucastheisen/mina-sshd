package org.apache.sftp.protocol.packetdata.openssh.impl;


import org.apache.sftp.protocol.PacketType;
import org.apache.sftp.protocol.impl.SftpProtocolBuffer;
import org.apache.sftp.protocol.packetdata.Status;
import org.apache.sftp.protocol.packetdata.openssh.HardLink;


public class DefaultHardLink extends AbstractExtendedPath<HardLink, Status> implements HardLink {
    private String targetPath;

    protected void appendToStringBuilder2( StringBuilder builder ) {
        builder.append( ",'targetPath'='" ).append( targetPath ).append( "'" );
    }

    @Override
    public Class<Status> expectedResponseType() {
        return Status.class;
    }

    @Override
    public String getExtendedRequest() {
        return "hardlink@openssh.com";
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.SSH_FXP_EXTENDED;
    }

    @Override
    public String getTargetPath() {
        return targetPath;
    }

    @Override
    public HardLink parseFrom2( SftpProtocolBuffer buffer ) {
        targetPath = buffer.getString();
        return this;
    }

    @Override
    public HardLink setTargetPath( String targetPath ) {
        this.targetPath = targetPath;
        return this;
    }

    @Override
    public void writeTo2( SftpProtocolBuffer buffer ) {
        buffer.putString( targetPath );
    }
}
