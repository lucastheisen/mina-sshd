package org.apache.sshd.sftp.client.packetdata;


import org.apache.sshd.sftp.Request;


public interface RealPath extends BasePath<RealPath>, Request<RealPath, Name> {
}
