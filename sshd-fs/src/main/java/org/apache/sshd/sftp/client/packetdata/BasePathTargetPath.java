package org.apache.sshd.sftp.client.packetdata;


public interface BasePathTargetPath<T> extends BasePath<T> {
    public String getTargetPath();

    public T setTargetPath( String path );
}
