package org.apache.sshd.sftp.impl;


import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.concurrent.locks.ReentrantLock;


import org.apache.sshd.sftp.PacketType;


public class SftpProtocolBuffer {
    private static final Charset UTF8 = Charset.forName( "UTF-8" );
    private static PackBuffer packBuffer = new PackBuffer();
    private static ReentrantLock packLock = new ReentrantLock();

    private ByteBuffer buffer;
    private byte[] stringBuffer;

    private SftpProtocolBuffer( ByteBuffer buffer ) {
        this.buffer = buffer;
    }

    public static SftpProtocolBuffer allocate( int size ) {
        return new SftpProtocolBuffer( ByteBuffer.allocate( size ) );
    }

    public static SftpProtocolBuffer allocateDirect( int size ) {
        return new SftpProtocolBuffer( ByteBuffer.allocateDirect( size ) );
    }

    private static SftpProtocolBuffer copy( SftpProtocolBuffer from ) {
        SftpProtocolBuffer to = new SftpProtocolBuffer(
                ByteBuffer.allocateDirect( from.remaining() ) );
        to.put( from );
        return to;
    }

    public SftpProtocolBuffer asReadOnlyBuffer() {
        return new SftpProtocolBuffer( this.buffer.asReadOnlyBuffer() );
    }

    public void clear() {
        buffer.clear();
    }

    public void ensureSize( int atLeast ) {
        int newSize = buffer.capacity() + Math.max( atLeast, buffer.capacity() );
        ByteBuffer newBuffer = ByteBuffer.allocateDirect( newSize );
        if ( buffer.isDirect() ) {
            newBuffer = ByteBuffer.allocateDirect( newSize );
        }
        else {
            newBuffer = ByteBuffer.allocate( newSize );
        }
        buffer.flip();
        newBuffer.put( buffer );
        buffer = newBuffer;
    }

    private int ensureStringBufferSize() {
        int size = buffer.getInt();
        if ( stringBuffer == null || stringBuffer.length < size ) {
            stringBuffer = new byte[size];
        }
        return size;
    }

    public void flip() {
        buffer.flip();
    }

    public void get( byte[] bytes ) {
        buffer.get( bytes );
    }

    public ByteBuffer getByteBuffer() {
        return buffer;
    }

    public int getInt() {
        return buffer.getInt();
    }

    public PacketType getPacketType() {
        return PacketType.fromValue( buffer.get() );
    }

    public String getString() {
        return newString( ensureStringBufferSize() );
    }

    public boolean hasRemaining() {
        return buffer.hasRemaining();
    }

    private String newString( int size ) {
        buffer.get( stringBuffer, 0, size );
        return new String( stringBuffer, 0, size, UTF8 );
    }

    public static SftpProtocolBuffer pack( PackCallback packCallback ) {
        try {
            packLock.lock();
            packCallback.pack( packBuffer );
            return packBuffer.pack();
        }
        finally {
            packLock.unlock();
        }
    }

    public void put( SftpProtocolBuffer buffer ) {
        this.buffer.put( buffer.buffer );
    }

    public void putInt( int value ) {
        buffer.putInt( value );
    }

    public void putPacketType( PacketType packetType ) {
        buffer.put( packetType.getValue() );
    }

    public void putString( String value ) {
        int size = value.length();
        buffer.putInt( size );
        if ( size > 0 ) {
            buffer.put( value.getBytes( UTF8 ) );
        }
    }

    public int remaining() {
        return buffer.remaining();
    }

    public static SftpProtocolBuffer wrap( ByteBuffer buffer ) {
        return new SftpProtocolBuffer( buffer );
    }

    public static interface PackCallback {
        public void pack( PackBuffer packBuffer );
    }

    public static class PackBuffer {
        private SftpProtocolBuffer buffer;

        private PackBuffer() {
            this.buffer = SftpProtocolBuffer.allocate( 65536 );
        }

        private SftpProtocolBuffer pack() {
            buffer.flip();
            SftpProtocolBuffer packed = SftpProtocolBuffer.copy( buffer );
            buffer.clear();
            return packed;
        }

        public void put( SftpProtocolBuffer buffer ) {
            this.buffer.ensureSize( this.buffer.remaining() + buffer.remaining() );
            this.buffer.put( buffer );
        }

        public void putInt( int value ) {
            this.buffer.ensureSize( buffer.remaining() + 4 );
            this.buffer.putInt( value );
        }

        public void putPacketType( PacketType packetType ) {
            this.buffer.ensureSize( buffer.remaining() + 1 );
            this.buffer.putPacketType( packetType );
        }

        public void putString( String value ) {
            this.buffer.ensureSize( buffer.remaining() + value.length() );
            this.buffer.putString( value );
        }
    }
}