package org.apache.sshd.sftp.impl;


import java.nio.ByteBuffer;


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

    @Override
    public PacketData parseFrom( ByteBuffer buffer ) {
        id = buffer.getInt();
        parseRequestFrom( buffer );
        return this;
    }

    abstract public PacketData parseRequestFrom( ByteBuffer buffer );

    abstract public void writeRequestTo( ByteBuffer buffer );

    @Override
    public void writeTo( ByteBuffer buffer ) {
        buffer.putInt( id );
        writeRequestTo( buffer );
    }
}
