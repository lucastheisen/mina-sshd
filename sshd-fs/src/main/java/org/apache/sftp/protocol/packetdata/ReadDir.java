package org.apache.sftp.protocol.packetdata;


import org.apache.sftp.protocol.Request;


public interface ReadDir extends BaseHandle<ReadDir>, Request<ReadDir, Name> {
}
