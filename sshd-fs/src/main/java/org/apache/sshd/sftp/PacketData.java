package org.apache.sshd.sftp;


import java.nio.charset.Charset;


import org.apache.sshd.sftp.impl.SftpProtocolBuffer;


public interface PacketData<T> {
    public static final Charset UTF_8 = Charset.forName( "UTF-8" );

    public PacketType getPacketType();

    public byte getPacketTypeByte();
    
    public T parseFrom( SftpProtocolBuffer buffer );

    public T setPacketTypeByte( byte packetDataTypeByte );

    public void writeTo( SftpProtocolBuffer buffer );
}
