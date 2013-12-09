package org.apache.sshd.sftp.impl;

import java.nio.ByteBuffer;


import org.apache.sshd.sftp.FileAttributes;
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
    public int getSize() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public FileAttributes getFileAttributes() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getPath() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PacketData parseRequestFrom( ByteBuffer buffer ) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void writeRequestTo( ByteBuffer buffer ) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public MkDir setFileAttributes() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public MkDir setPath( String path ) {
        // TODO Auto-generated method stub
        return null;
    }
}
