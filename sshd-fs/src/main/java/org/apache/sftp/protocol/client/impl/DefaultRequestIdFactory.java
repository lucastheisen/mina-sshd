package org.apache.sftp.protocol.client.impl;

import org.apache.sftp.protocol.client.RequestIdFactory;

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
