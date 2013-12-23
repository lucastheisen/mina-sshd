package org.apache.sftp.protocol.packetdata;


import org.apache.sftp.protocol.Request;
import org.apache.sftp.protocol.Response;


public interface Raw extends BaseData<Raw>, Request<Raw, Raw>, Response<Raw> {
}
