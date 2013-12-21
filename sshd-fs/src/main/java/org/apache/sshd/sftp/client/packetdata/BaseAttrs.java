package org.apache.sshd.sftp.client.packetdata;


import org.apache.sshd.sftp.PacketData;
import org.apache.sshd.sftp.impl.SftpFileAttributes;


public interface BaseAttrs<T> extends PacketData<T> {
    public SftpFileAttributes getFileAttributes();

    public T setFileAttributes( SftpFileAttributes fileAttributes );
}
