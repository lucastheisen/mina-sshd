package org.apache.sftp.protocol.packetdata.openssh;


import org.apache.sftp.protocol.packetdata.Status;


/**
 * Implements the hardlink operation:
 * 
 * <pre>
 * 10. sftp: Extension request "hardlink@openssh.com"
 * 
 * This request is for creating a hard link to a regular file. This
 * request is implemented as a SSH_FXP_EXTENDED request with the
 * following format:
 * 
 *         uint32          id
 *         string          "hardlink@openssh.com"
 *         string          oldpath
 *         string          newpath
 * 
 * On receiving this request the server will perform the operation
 * link(oldpath, newpath) and will respond with a SSH_FXP_STATUS message.
 * This extension is advertised in the SSH_FXP_VERSION hello with version
 * "1".
 * </pre>
 * 
 * @author Lucas Theisen
 * @see <a href=
 *      "http://www.openbsd.org/cgi-bin/cvsweb/src/usr.bin/ssh/PROTOCOL?rev=HEAD;content-type=text/plain"
 *      >OpenSSH's deviations and extensions to the published SSH protocol</a>
 */
public interface HardLink extends BaseExtendedPath<HardLink, Status> {
    public String getTargetPath();

    public HardLink setTargetPath( String targetPath );
}
