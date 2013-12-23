package org.apache.sftp.protocol.packetdata.openssh.impl;


import org.apache.sftp.protocol.Response;
import org.apache.sftp.protocol.impl.SftpProtocolBuffer;
import org.apache.sftp.protocol.packetdata.Extended;
import org.apache.sftp.protocol.packetdata.impl.AbstractPacketData;
import org.apache.sftp.protocol.packetdata.openssh.BaseExtendedPath;


public abstract class AbstractExtendedPath<T extends Extended<T, S>, S extends Response<S>>
        extends AbstractPacketData<T>
        implements BaseExtendedPath<T, S> {
    private String path;

    @Override
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

    @Override
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
