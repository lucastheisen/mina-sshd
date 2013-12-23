package org.apache.sftp.protocol;


public class UnexpectedPacketDataException extends Exception {
    private static final long serialVersionUID = -4224331131903158925L;

    private PacketData<?> response;

    public UnexpectedPacketDataException( PacketData<?> response ) {
        this.response = response;
    }

    public PacketData<?> getResponse() {
        return response;
    }
    
    @Override
    public String getMessage() {
        return "unexpected PacketData " + response.getClass().getName() + ": " + response.toString();
    }
}
