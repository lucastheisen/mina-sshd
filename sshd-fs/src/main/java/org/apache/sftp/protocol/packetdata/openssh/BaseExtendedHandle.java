package org.apache.sftp.protocol.packetdata.openssh;


import org.apache.sftp.protocol.Response;
import org.apache.sftp.protocol.packetdata.BaseHandle;
import org.apache.sftp.protocol.packetdata.Extended;


public interface BaseExtendedHandle<T extends Extended<T, S>, S extends Response<S>> extends Extended<T, S>, BaseHandle<T> {
}
