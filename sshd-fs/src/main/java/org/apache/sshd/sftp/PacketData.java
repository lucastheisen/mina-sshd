package org.apache.sshd.sftp;


import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;


public interface PacketData {
    public static final Charset UTF_8 = Charset.forName( "UTF-8" );

    public PacketType getPacketType();
    
    public int getSize();

    public PacketData parseFrom( ByteBuffer buffer );

    public void writeTo( ByteBuffer buffer ) throws IOException;
}
