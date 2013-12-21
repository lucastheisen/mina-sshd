package org.apache.sshd.sftp.client.packetdata;


import org.apache.sshd.sftp.Request;


public interface MkDir extends BaseAttrs<MkDir>, BasePath<MkDir>, Request<MkDir, Status> {
}
