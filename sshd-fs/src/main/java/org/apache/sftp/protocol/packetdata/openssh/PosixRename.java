package org.apache.sftp.protocol.packetdata.openssh;


import org.apache.sftp.protocol.packetdata.Extended;
import org.apache.sftp.protocol.packetdata.Status;


public interface PosixRename extends Extended<PosixRename, Status>, BaseExtendedPath<PosixRename, Status> {
    public String getTargetPath();
    
    public PosixRename setTargetPath( String targetPath );
}
