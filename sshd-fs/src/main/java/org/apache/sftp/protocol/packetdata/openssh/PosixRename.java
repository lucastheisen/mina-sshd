package org.apache.sftp.protocol.packetdata.openssh;


import org.apache.sftp.protocol.packetdata.Status;


public interface PosixRename extends BaseExtendedPath<PosixRename, Status> {
    public String getTargetPath();

    public PosixRename setTargetPath( String targetPath );
}
