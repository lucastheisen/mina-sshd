package org.apache.sshd.sftp.client.packetdata;


public interface BaseHandleOffset<T> extends BaseHandle<T> {
    public long getOffset();

    public T setOffset( long offset );
}
