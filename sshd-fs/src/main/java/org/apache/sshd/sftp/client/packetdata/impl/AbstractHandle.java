package org.apache.sshd.sftp.client.packetdata.impl;


import org.apache.sshd.sftp.impl.SftpProtocolBuffer;


abstract public class AbstractHandle<T>
        extends AbstractPacketData<T> {
    private byte[] handle;

    protected void appendToStringBuilder2( StringBuilder builder ) {
    }

    @Override
    protected void appendToStringBuilder( StringBuilder builder ) {
        builder.append( "'handle':[" );
        boolean first = true;
        for ( byte bite : handle ) {
            if ( first ) {
                first = false;
            }
            else {
                builder.append( ", " );
            }
            builder.append( bite );
        }
        builder.append( "]" );
        appendToStringBuilder2( builder );
    }

    public byte[] getHandle() {
        return handle;
    }

    @SuppressWarnings("unchecked")
    protected T parseFrom2( SftpProtocolBuffer buffer ) {
        return (T)this;
    }

    @Override
    public T parseFrom( SftpProtocolBuffer buffer ) {
        this.handle = buffer.getByteString();
        return parseFrom2( buffer );
    }

    @SuppressWarnings("unchecked")
    public T setHandle( byte[] handle ) {
        this.handle = handle;
        return (T)this;
    }

    protected void writeTo2( SftpProtocolBuffer buffer ) {
    }

    @Override
    public void writeTo( SftpProtocolBuffer buffer ) {
        buffer.putByteString( handle );
        writeTo2( buffer );
    }
}
