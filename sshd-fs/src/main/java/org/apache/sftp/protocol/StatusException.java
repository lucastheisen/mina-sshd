package org.apache.sftp.protocol;


import org.apache.sftp.protocol.packetdata.Status;


public class StatusException extends Exception {
    private static final long serialVersionUID = -1976222606798431110L;

    private Status status;

    public StatusException( Status status ) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return status.getMessage();
    }
}
