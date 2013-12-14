package org.apache.sshd.sftp;


import java.io.IOException;
import java.nio.charset.Charset;


import org.apache.sshd.sftp.impl.SftpProtocolBuffer;


public interface PacketData {
    public static final Charset UTF_8 = Charset.forName( "UTF-8" );

    public PacketType getPacketType();

    public int getSize();

    public PacketData parseFrom( SftpProtocolBuffer buffer );

    public void writeTo( SftpProtocolBuffer buffer ) throws IOException;
}
