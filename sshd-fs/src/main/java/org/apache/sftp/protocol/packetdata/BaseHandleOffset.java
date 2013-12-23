package org.apache.sftp.protocol.packetdata;


public interface BaseHandleOffset<T> extends BaseHandle<T> {
    public long getOffset();

    public T setOffset( long offset );
}
