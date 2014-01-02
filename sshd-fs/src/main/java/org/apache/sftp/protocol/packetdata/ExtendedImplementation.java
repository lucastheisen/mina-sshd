package org.apache.sftp.protocol.packetdata;


import org.apache.sftp.protocol.Response;


public interface ExtendedImplementation<T extends Extended<T, S>, S extends Response<S>>
        extends Implementation<T> {
    public String getExtendedRequest();
    
    public Implementation<S> getExtendedReplyImplementation();
}
