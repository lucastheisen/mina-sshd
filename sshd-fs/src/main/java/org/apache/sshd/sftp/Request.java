package org.apache.sshd.sftp;


public interface Request<T, S extends Response<S>> extends PacketData<T> {
    public Class<S> expectedResponseType();
}
