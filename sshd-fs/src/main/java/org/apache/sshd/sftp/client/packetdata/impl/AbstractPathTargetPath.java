package org.apache.sshd.sftp.client.packetdata.impl;


import org.apache.sshd.sftp.impl.SftpProtocolBuffer;


abstract public class AbstractPathTargetPath<T>
        extends AbstractPath<T> {
    private String targetPath;

    protected void appendToStringBuilder2( StringBuilder builder ) {
        builder.append( ",'targetPath':'" ).append( targetPath ).append( "'" );
    }

    public String getTargetPath() {
        return targetPath;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected T parseFrom2( SftpProtocolBuffer buffer ) {
        targetPath = buffer.getString();
        return (T)this;
    }

    @SuppressWarnings("unchecked")
    public T setTargetPath( String targetPath ) {
        this.targetPath = targetPath;
        return (T)this;
    }

    @Override
    protected void writeTo2( SftpProtocolBuffer buffer ) {
        buffer.putString( targetPath );
    }
}
