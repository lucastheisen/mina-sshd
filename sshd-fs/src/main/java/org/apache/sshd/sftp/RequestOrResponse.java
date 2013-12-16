package org.apache.sshd.sftp;


public interface RequestOrResponse<T> extends PacketData<T> {
    /**
     * Returns the id that was set by the RequestProcessor when this request
     * was sent or response was received.
     * 
     * @return The id of the request
     */
    public int getId();

    /**
     * Will be set by the RequestProcessor before the request is sent and when
     * a response is returned.
     * 
     * @param id The id of the request
     * @return This instance for chaining
     */
    public T setId( int id );
}
