package org.apache.sftp.protocol.packetdata;


import org.apache.sftp.protocol.Request;


public interface OpenDir extends BasePath<OpenDir>, Request<OpenDir, Handle> {
}
