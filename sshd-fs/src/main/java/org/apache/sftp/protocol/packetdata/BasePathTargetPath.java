package org.apache.sftp.protocol.packetdata;


public interface BasePathTargetPath<T> extends BasePath<T> {
    public String getTargetPath();

    public T setTargetPath( String path );
}
