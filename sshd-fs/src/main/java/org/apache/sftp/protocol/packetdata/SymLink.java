package org.apache.sftp.protocol.packetdata;


import org.apache.sftp.protocol.Request;


public interface SymLink extends BasePathTargetPath<SymLink>, Request<SymLink, Status> {
}
