package org.apache.sftp.protocol.packetdata;


import org.apache.sftp.protocol.PacketData;
import org.apache.sftp.protocol.impl.SftpFileAttributes;


public interface BaseAttrs<T> extends PacketData<T> {
    public SftpFileAttributes getFileAttributes();

    public T setFileAttributes( SftpFileAttributes fileAttributes );
}
