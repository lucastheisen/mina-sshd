package org.apache.sftp.protocol.packetdata.impl;


import org.apache.sftp.protocol.impl.SftpProtocolBuffer;


abstract public class AbstractPath<T>
        extends AbstractPacketData<T> {
    private String path;

    protected void appendToStringBuilder2( StringBuilder builder ) {
    }

    @Override
    protected void appendToStringBuilder( StringBuilder builder ) {
        builder.append( ",'path':'" ).append( path ).append( "'" );
        appendToStringBuilder2( builder );
    }

    public String getPath() {
        return path;
    }

    @SuppressWarnings("unchecked")
    protected T parseFrom2( SftpProtocolBuffer buffer ) {
        return (T)this;
    }

    @Override
    public T parseFrom( SftpProtocolBuffer buffer ) {
        path = buffer.getString();
        return parseFrom2( buffer );
    }

    @SuppressWarnings("unchecked")
    public T setPath( String path ) {
        this.path = path;
        return (T)this;
    }

    protected void writeTo2( SftpProtocolBuffer buffer ) {
    }

    @Override
    public void writeTo( SftpProtocolBuffer buffer ) {
        buffer.putString( path );
        writeTo2( buffer );
    }
}
