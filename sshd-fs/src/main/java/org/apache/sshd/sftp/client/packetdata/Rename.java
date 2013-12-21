package org.apache.sshd.sftp.client.packetdata;


import org.apache.sshd.sftp.Request;


public interface Rename extends BasePathTargetPath<Rename>, Request<Rename, Status> {
}
