package org.apache.sshd.sftp.impl;


import org.apache.sshd.sftp.PacketData;
import org.apache.sshd.sftp.Request;


public abstract class AbstractRequest implements Request {
    int id;

    AbstractRequest( int id ) {
        this.id = id;
    };

    @Override
    public int getId() {
        return id;
    }
    
    abstract public int getRequestSize();

    @Override
    public int getSize() {
        return 4 + getRequestSize();
    }

    @Override
    public PacketData parseFrom( SftpProtocolBuffer buffer ) {
        id = buffer.getInt();
        return parseRequestFrom( buffer );
    }

    abstract public PacketData parseRequestFrom( SftpProtocolBuffer buffer );

    abstract public void writeRequestTo( SftpProtocolBuffer buffer );

    @Override
    public void writeTo( SftpProtocolBuffer buffer ) {
        buffer.putInt( id );
        writeRequestTo( buffer );
    }
}
