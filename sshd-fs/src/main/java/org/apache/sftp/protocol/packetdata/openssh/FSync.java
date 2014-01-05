package org.apache.sftp.protocol.packetdata.openssh;


import org.apache.sftp.protocol.packetdata.Status;


/**
 * Implements the fsync command:
 * 
 * <pre>
 * 10. sftp: Extension request "fsync@openssh.com"
 * 
 * This request asks the server to call fsync(2) on an open file handle.
 * 
 *         uint32          id
 *         string          "fsync@openssh.com"
 *         string          handle
 * 
 * One receiving this request, a server will call fsync(handle_fd) and will
 * respond with a SSH_FXP_STATUS message.
 * 
 * This extension is advertised in the SSH_FXP_VERSION hello with version
 * "1".
 * </pre>
 * 
 * @author Lucas Theisen
 * @see <a href=
 *      "http://www.openbsd.org/cgi-bin/cvsweb/src/usr.bin/ssh/PROTOCOL?rev=HEAD;content-type=text/plain"
 *      >OpenSSH's deviations and extensions to the published SSH protocol</a>
 */
public interface FSync extends BaseExtendedHandle<FSync, Status> {

}
