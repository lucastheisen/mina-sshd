package org.apache.sftp.protocol.packetdata.openssh.impl;


import org.apache.sftp.protocol.Response;
import org.apache.sftp.protocol.packetdata.Extended;
import org.apache.sftp.protocol.packetdata.impl.AbstractPath;
import org.apache.sftp.protocol.packetdata.openssh.BaseExtendedPath;


public abstract class AbstractExtendedPath<T extends Extended<T, S>, S extends Response<S>>
        extends AbstractPath<T>
        implements BaseExtendedPath<T, S> {
    protected void appendToStringBuilder( StringBuilder builder ) {
        builder.append( ",'extendedRequest'='" ).append( getExtendedRequest() )
                .append( "','path'='" ).append( getPath() ).append( "'" );
        appendToStringBuilder2( builder );
    }
}
