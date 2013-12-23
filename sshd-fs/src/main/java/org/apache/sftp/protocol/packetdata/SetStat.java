package org.apache.sftp.protocol.packetdata;


import org.apache.sftp.protocol.Request;


public interface SetStat extends Request<SetStat, Status>, BasePath<SetStat>, BaseAttrs<SetStat> {
}
