package org.apache.sshd.sftp.impl;


import java.nio.ByteBuffer;
import java.nio.charset.Charset;


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
    
    public byte[] getByteString() {
        return newByteString( ensureStringBufferSize() );
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

    public Placeholder<Void> newSizePlaceholder() {
        return new SizePlaceholder( buffer );
    }
    
    private byte[] newByteString( int size ) {
        buffer.get( stringBuffer, 0, size );
        return stringBuffer;
    }

    private String newString( int size ) {
        return new String( newByteString( size ), 0, size, UTF8 );
    }

    public SftpProtocolBuffer put( byte oneByte ) {
        this.buffer.put( oneByte );
        return this;
    }

    public SftpProtocolBuffer put( byte[] bytes ) {
        this.buffer.put( bytes );
        return this;
    }

    public SftpProtocolBuffer put( SftpProtocolBuffer buffer ) {
        this.buffer.put( buffer.buffer );
        return this;
    }

    public SftpProtocolBuffer put( Placeholder<?> placeholder ) {
        placeholder.setPosition();
        return this;
    }

    public SftpProtocolBuffer putByteString( byte[] value ) {
        buffer.putInt( value.length );
        if ( value.length > 0 ) {
            buffer.put( value );
        }
        return this;
    }

    public SftpProtocolBuffer putInt( int value ) {
        buffer.putInt( value );
        return this;
    }

    public SftpProtocolBuffer putLong( long value ) {
        buffer.putLong( value );
        return this;
    }

    public SftpProtocolBuffer putString( String value ) {
        return putByteString( value.getBytes( UTF8 ) );
    }

    public int remaining() {
        return buffer.remaining();
    }

    public static SftpProtocolBuffer wrap( ByteBuffer buffer ) {
        return new SftpProtocolBuffer( buffer );
    }

    public static interface Placeholder<T> {
        public void setPosition();

        public void setValue( T value );
    }

    public static class SizePlaceholder implements Placeholder<Void> {
        private ByteBuffer buffer;
        private int position;
        private int sizeStart;

        private SizePlaceholder( ByteBuffer buffer ) {
            this.buffer = buffer;
        }

        public void setPosition() {
            this.position = buffer.position();
            this.sizeStart = this.position + 4;
            buffer.position( this.sizeStart );
        }

        @Override
        public void setValue( Void value ) {
            buffer.putInt( position, buffer.position() - sizeStart );
        }
    }
}