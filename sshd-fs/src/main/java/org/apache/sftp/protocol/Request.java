package org.apache.sftp.protocol;


public interface Request<T, S extends Response<S>> extends PacketData<T> {
    public Class<S> expectedResponseType();
}
