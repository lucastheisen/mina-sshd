package org.apache.sftp.protocol.packetdata;


import org.apache.sftp.protocol.Request;


public interface MkDir extends BaseAttrs<MkDir>, BasePath<MkDir>, Request<MkDir, Status> {
}
