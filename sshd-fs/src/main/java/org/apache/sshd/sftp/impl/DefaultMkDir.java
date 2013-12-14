package org.apache.sshd.sftp.impl;


import org.apache.sshd.sftp.PacketData;
import org.apache.sshd.sftp.PacketType;
import org.apache.sshd.sftp.client.packetdata.MkDir;


public class DefaultMkDir extends AbstractRequest implements MkDir {

    DefaultMkDir( int id ) {
        super( id );
    }

    @Override
    public PacketType getPacketType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SftpFileAttributes getFileAttributes() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getPath() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PacketData parseRequestFrom( SftpProtocolBuffer buffer ) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void writeRequestTo( SftpProtocolBuffer buffer ) {
        // TODO Auto-generated method stub

    }

    @Override
    public MkDir setFileAttributes( SftpFileAttributes sftpFileAttributes ) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public MkDir setPath( String path ) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getRequestSize() {
        // TODO Auto-generated method stub
        return 0;
    }
}
