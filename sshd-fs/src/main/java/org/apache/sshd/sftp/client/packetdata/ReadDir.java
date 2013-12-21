package org.apache.sshd.sftp.client.packetdata;


import org.apache.sshd.sftp.Request;


public interface ReadDir extends BaseHandle<ReadDir>, Request<ReadDir, Name> {
}
