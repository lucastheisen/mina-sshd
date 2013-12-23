package org.apache.sftp.protocol.packetdata;


import org.apache.sftp.protocol.Request;


public interface Rename extends BasePathTargetPath<Rename>, Request<Rename, Status> {
}
