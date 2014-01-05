package org.apache.sftp.protocol.packetdata.openssh;


import org.apache.sftp.protocol.packetdata.Status;


/**
 * Implements the rename operation with POSIX semantics:
 * 
 * <pre>
 * 3.3. sftp: Extension request "posix-rename@openssh.com"
 * 
 * This operation provides a rename operation with POSIX semantics, which
 * are different to those provided by the standard SSH_FXP_RENAME in
 * draft-ietf-secsh-filexfer-02.txt. This request is implemented as a
 * SSH_FXP_EXTENDED request with the following format:
 * 
 *         uint32          id
 *         string          "posix-rename@openssh.com"
 *         string          oldpath
 *         string          newpath
 * 
 * On receiving this request the server will perform the POSIX operation
 * rename(oldpath, newpath) and will respond with a SSH_FXP_STATUS message.
 * This extension is advertised in the SSH_FXP_VERSION hello with version
 * "1".
 * </pre>
 * 
 * @author Lucas Theisen
 * @see <a href=
 *      "http://www.openbsd.org/cgi-bin/cvsweb/src/usr.bin/ssh/PROTOCOL?rev=HEAD;content-type=text/plain"
 *      >OpenSSH's deviations and extensions to the published SSH protocol</a>
 */
public interface PosixRename extends BaseExtendedPath<PosixRename, Status> {
    public String getTargetPath();

    public PosixRename setTargetPath( String targetPath );
}
