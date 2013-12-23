package org.apache.sftp.protocol.packetdata;


import org.apache.sftp.protocol.Request;


public interface Close extends BaseHandle<Close>, Request<Close, Status> {
}
