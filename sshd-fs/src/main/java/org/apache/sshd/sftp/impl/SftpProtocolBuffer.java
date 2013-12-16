package org.apache.sshd.sftp.impl;


import java.nio.ByteBuffer;
import java.nio.charset.Charset;


import org.apache.sshd.sftp.PacketData;


public class SftpProtocolBuffer {
    private static final Charset UTF8 = Charset.forName( "UTF-8" );

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

    public byte get() {
        return buffer.get();
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

    public long getLong() {
        return buffer.getLong();
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

    public void put( byte[] bytes ) {
        this.buffer.put( bytes );
    }

    public void put( SftpProtocolBuffer buffer ) {
        this.buffer.put( buffer.buffer );
    }

    public void putInt( int value ) {
        buffer.putInt( value );
    }

    public void putLong( long value ) {
        buffer.putLong( value );
    }

    public void putPacket( PacketData<?> packetData ) {
        int start = buffer.position();
        int packetStart = start + 4;
        buffer.position( packetStart );
        buffer.put( packetData.getPacketTypeByte() );
        packetData.writeTo( this );
        buffer.putInt( start, buffer.position() - packetStart );
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
}