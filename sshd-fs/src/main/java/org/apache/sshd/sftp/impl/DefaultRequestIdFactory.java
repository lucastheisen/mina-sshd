package org.apache.sshd.sftp.impl;

import org.apache.sshd.sftp.RequestIdFactory;

public class DefaultRequestIdFactory implements RequestIdFactory {
    private int nextId = Integer.MIN_VALUE;

    @Override
    public int nextId() {
        int nextId = this.nextId;
        this.nextId = ( this.nextId == Integer.MAX_VALUE )
                ? Integer.MIN_VALUE
                : this.nextId + 1;
        return nextId;
    }

}
