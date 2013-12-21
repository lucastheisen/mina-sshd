package org.apache.sshd.sftp.client.packetdata;


import org.apache.sshd.sftp.Request;
import org.apache.sshd.sftp.Response;


public interface Raw extends BaseData<Raw>, Request<Raw, Raw>, Response<Raw> {
}
