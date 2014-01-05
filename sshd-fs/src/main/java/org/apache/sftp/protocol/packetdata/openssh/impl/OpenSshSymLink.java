package org.apache.sftp.protocol.packetdata.openssh.impl;


import org.apache.sftp.protocol.PacketType;
import org.apache.sftp.protocol.impl.SftpProtocolBuffer;
import org.apache.sftp.protocol.packetdata.Status;
import org.apache.sftp.protocol.packetdata.SymLink;
import org.apache.sftp.protocol.packetdata.impl.AbstractPathTargetPath;


/**
 * Implement OpenSSH's reversed argument version of SymLink.
 * 
 * <pre>
 * 3.1. sftp: Reversal of arguments to SSH_FXP_SYMLINK
 * 
 * When OpenSSH's sftp-server was implemented, the order of the arguments
 * to the SSH_FXP_SYMLINK method was inadvertently reversed. Unfortunately,
 * the reversal was not noticed until the server was widely deployed. Since
 * fixing this to follow the specification would cause incompatibility, the
 * current order was retained. For correct operation, clients should send
 * SSH_FXP_SYMLINK as follows:
 * 
 *         uint32          id
 *         string          targetpath
 *         string          linkpath
 * </pre>
 * 
 * @author Lucas Theisen
 * @see <a href=
 *      "http://www.openbsd.org/cgi-bin/cvsweb/src/usr.bin/ssh/PROTOCOL?rev=HEAD;content-type=text/plain"
 *      >OpenSSH's deviations and extensions to the published SSH protocol</a>
 */
public class OpenSshSymLink extends AbstractPathTargetPath<SymLink> implements SymLink {
    @Override
    public Class<Status> expectedResponseType() {
        return Status.class;
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.SSH_FXP_SYMLINK;
    }

    @Override
    public SymLink parseFrom( SftpProtocolBuffer buffer ) {
        setTargetPath( buffer.getString() );
        setPath( buffer.getString() );
        return parseFrom2( buffer );
    }

    @Override
    public void writeTo( SftpProtocolBuffer buffer ) {
        buffer.putString( getTargetPath() );
        buffer.putString( getPath() );
        writeTo2( buffer );
    }
}
