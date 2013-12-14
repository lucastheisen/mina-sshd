package org.apache.sshd.sftp.impl;


import org.apache.sshd.sftp.PacketData;
import org.apache.sshd.sftp.PacketType;
import org.apache.sshd.sftp.client.packetdata.OpenDir;
import org.apache.sshd.sftp.impl.SftpProtocolBuffer.PackBuffer;
import org.apache.sshd.sftp.impl.SftpProtocolBuffer.PackCallback;


public class DefaultOpenDir extends AbstractRequest implements OpenDir {
    private SftpProtocolBuffer data;
    private String path;

    public DefaultOpenDir( int id, String path ) {
        super( id );
        this.path = path;
    }
    
    private SftpProtocolBuffer getData() {
        if ( data == null ) {
            data = SftpProtocolBuffer.pack( new PackCallback() {
                @Override
                public void pack( PackBuffer packBuffer ) {
                    packBuffer.putString( path );
                }
            } );
        }
        return data;
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.SSH_FXP_OPENDIR;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public int getRequestSize() {
        return getData().remaining();
    }

    @Override
    public PacketData parseRequestFrom( SftpProtocolBuffer buffer ) {
        path = buffer.getString();
        return this;
    }

    @Override
    public void setPath( String path ) {
        this.path = path;
    }

    @Override
    public void writeRequestTo( SftpProtocolBuffer buffer ) {
        buffer.putString( path );
    }
}
