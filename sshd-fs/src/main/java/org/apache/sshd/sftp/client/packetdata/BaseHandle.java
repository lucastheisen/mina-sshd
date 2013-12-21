package org.apache.sshd.sftp.client.packetdata;


import org.apache.sshd.sftp.PacketData;


public interface BaseHandle<T> extends PacketData<T> {
    public byte[] getHandle();

    public T setHandle( byte[] handle );
}
