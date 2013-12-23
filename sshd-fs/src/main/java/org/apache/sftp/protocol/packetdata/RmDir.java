package org.apache.sftp.protocol.packetdata;


import org.apache.sftp.protocol.Request;


public interface RmDir extends BasePath<RmDir>, Request<RmDir, Status> {
}
