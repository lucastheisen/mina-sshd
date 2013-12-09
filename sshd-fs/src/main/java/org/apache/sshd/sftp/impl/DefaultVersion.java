package org.apache.sshd.sftp.impl;


import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


import org.apache.sshd.sftp.PacketData;
import org.apache.sshd.sftp.PacketType;
import org.apache.sshd.sftp.client.packetdata.Version;


public class DefaultVersion implements PacketData, Version {
    private ByteBuffer data;
    private Map<String, String> extensions;
    private int version;

    public DefaultVersion() {
        extensions = new HashMap<>();
    }

    @Override
    public DefaultVersion addExtension( String name, String data ) {
        extensions.put( name, data );
        data = null;
        return this;
    }

    private ByteBuffer getData() {
        if ( data == null ) {
            data = ByteBuffer.allocate( 34000 );
            data.putInt( version );
            for ( Map.Entry<String, String> extension : extensions.entrySet() ) {
                String extensionName = extension.getKey();
                data.putInt( extensionName.length() );
                data.put( extensionName.getBytes( UTF_8 ) );

                String extensionData = extension.getValue();
                if ( extensionData == null || extensionData.isEmpty() ) {
                    data.putInt( 0 );
                }
                else {
                    data.putInt( extensionData.length() );
                    data.put( extensionData.getBytes( UTF_8 ) );
                }
            }
            data.flip();
        }
        return data.asReadOnlyBuffer();
    }

    @Override
    public Map<String, String> getExtensions() {
        return Collections.unmodifiableMap( extensions );
    }

    public PacketType getPacketType() {
        return PacketType.SSH_FXP_INIT;
    }

    public int getSize() {
        return getData().remaining();
    }

    @Override
    public int getVersion() {
        return version;
    }

    @Override
    public DefaultVersion parseFrom( ByteBuffer buffer ) {
        this.data = buffer;
        version = data.getInt();
        if ( data.hasRemaining() ) {
            String extensionName = null;
            String extensionData = null;
            byte[] bytes = new byte[data.remaining()];
            while ( data.hasRemaining() ) {
                // extension name
                int stringSize = data.getInt();
                data.get( bytes, 0, stringSize );
                extensionName = new String( bytes, 0, stringSize, UTF_8 );

                // extension data
                stringSize = data.getInt();
                if ( stringSize == 0 ) {
                    extensionData = "";
                }
                else {
                    data.get( bytes, 0, stringSize );
                    extensionData = new String( bytes, 0, stringSize, UTF_8 );
                }

                extensions.put( extensionName, extensionData );
            }
        }

        return this;
    }

    @Override
    public DefaultVersion setVersion( int version ) {
        this.version = version;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder( "{'version':'" )
                .append( version )
                .append( "','extensions':{" );
        if ( !extensions.isEmpty() ) {
            boolean first = true;
            for ( Map.Entry<String, String> extension : extensions.entrySet() ) {
                if ( first ) {
                    first = false;
                }
                else {
                    builder.append( "," );
                }
                builder.append( "'" )
                        .append( extension.getKey() )
                        .append( "':'" )
                        .append( extension.getValue() )
                        .append( "'" );
            }
        }
        return builder.append( "}" ).toString();
    }

    @Override
    public void writeTo( ByteBuffer buffer ) throws IOException {
        buffer.put( getData() );
    }
}
