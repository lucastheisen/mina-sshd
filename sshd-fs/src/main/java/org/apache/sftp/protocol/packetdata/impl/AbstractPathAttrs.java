package org.apache.sftp.protocol.packetdata.impl;


import org.apache.sftp.protocol.impl.SftpFileAttributes;
import org.apache.sftp.protocol.impl.SftpProtocolBuffer;


abstract public class AbstractPathAttrs<T>
        extends AbstractPath<T> {
    private SftpFileAttributes fileAttributes;
    
    protected AbstractPathAttrs() {
        fileAttributes = new SftpFileAttributes();
    }

    protected void appendToStringBuilder2( StringBuilder builder ) {
        builder.append( ",'fileAttributes':" ).append( fileAttributes.toString() );
    }

    public SftpFileAttributes getFileAttributes() {
        return fileAttributes;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected T parseFrom2( SftpProtocolBuffer buffer ) {
        fileAttributes = new SftpFileAttributes().parseFrom( buffer );
        return (T)this;
    }

    @SuppressWarnings("unchecked")
    public T setFileAttributes( SftpFileAttributes fileAttributes ) {
        if ( fileAttributes == null ) {
            this.fileAttributes = new SftpFileAttributes();
        }
        else {
            this.fileAttributes = fileAttributes;
        }
        return (T)this;
    }

    @Override
    protected void writeTo2( SftpProtocolBuffer buffer ) {
        fileAttributes.writeTo( buffer );
    }
}
