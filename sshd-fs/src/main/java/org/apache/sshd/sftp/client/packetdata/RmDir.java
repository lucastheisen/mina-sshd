package org.apache.sshd.sftp.client.packetdata;


import org.apache.sshd.sftp.Request;


public interface RmDir extends BasePath<RmDir>, Request<RmDir, Status> {
}
