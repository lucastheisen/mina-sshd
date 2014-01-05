package org.apache.sftp.protocol.packetdata.openssh.impl;


import org.apache.sftp.protocol.Response;
import org.apache.sftp.protocol.packetdata.Extended;
import org.apache.sftp.protocol.packetdata.impl.AbstractHandle;
import org.apache.sftp.protocol.packetdata.openssh.BaseExtendedHandle;


public abstract class AbstractExtendedHandle<T extends Extended<T, S>, S extends Response<S>>
        extends AbstractHandle<T>
        implements BaseExtendedHandle<T, S> {
    protected void appendToStringBuilder( StringBuilder builder ) {
        builder.append( ",'extendedRequest'='" ).append( getExtendedRequest() )
                .append( "','handle'=" ).append( getHandle() ).append( "" );
        appendToStringBuilder2( builder );
    }
}
