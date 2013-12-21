package org.apache.sshd.sftp.client.packetdata.impl;


import org.apache.sshd.sftp.impl.SftpProtocolBuffer;


abstract public class AbstractHandleOffset<T>
        extends AbstractHandle<T> {
    private long offset;
    
    protected void appendToStringBuilder3( StringBuilder builder ) {
    }

    @Override
    protected void appendToStringBuilder2( StringBuilder builder ) {
        builder.append( "'offset':" ).append( offset );
        appendToStringBuilder3( builder );
    }

    public long getOffset() {
        return offset;
    }

    @SuppressWarnings("unchecked")
    protected T parseFrom3( SftpProtocolBuffer buffer ) {
        return (T)this;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected T parseFrom2( SftpProtocolBuffer buffer ) {
        offset = buffer.getLong();
        parseFrom3( buffer );
        return (T)this;
    }

    @SuppressWarnings("unchecked")
    public T setOffset( long offset ) {
        this.offset = offset;
        return (T)this;
    }
    
    protected void writeTo3( SftpProtocolBuffer buffer ) {
    }

    @Override
    protected void writeTo2( SftpProtocolBuffer buffer ) {
        buffer.putLong( offset );
        writeTo3( buffer );
    }
}
