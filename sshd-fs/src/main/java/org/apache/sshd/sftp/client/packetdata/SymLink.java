package org.apache.sshd.sftp.client.packetdata;


import org.apache.sshd.sftp.Request;


public interface SymLink extends BasePathTargetPath<SymLink>, Request<SymLink, Status> {
}
