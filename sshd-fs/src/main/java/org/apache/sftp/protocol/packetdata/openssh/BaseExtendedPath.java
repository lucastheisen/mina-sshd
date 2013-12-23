package org.apache.sftp.protocol.packetdata.openssh;


import org.apache.sftp.protocol.Response;
import org.apache.sftp.protocol.packetdata.Extended;


public interface BaseExtendedPath<T extends Extended<T, S>, S extends Response<S>> extends Extended<T, S> {
    public String getPath();

    public T setPath( String path );
}
