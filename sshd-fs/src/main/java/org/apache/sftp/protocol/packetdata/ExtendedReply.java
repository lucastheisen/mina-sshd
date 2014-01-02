package org.apache.sftp.protocol.packetdata;


import org.apache.sftp.protocol.PacketData;
import org.apache.sftp.protocol.Response;


public interface ExtendedReply<T> extends PacketData<T>, Response<T> {
}
