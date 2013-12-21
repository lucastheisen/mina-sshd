package org.apache.sshd.sftp;


import org.apache.sshd.sftp.client.packetdata.Status;


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
