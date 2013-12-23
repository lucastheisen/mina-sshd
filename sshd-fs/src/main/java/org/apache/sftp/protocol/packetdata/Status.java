package org.apache.sftp.protocol.packetdata;


import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


import org.apache.sftp.protocol.Response;


public interface Status extends Response<Status> {
    public Code getCode();

    public String getMessage();

    public Locale getMessageLocale();

    public Status setCode( Code code );

    public Status setMessage( String message );

    public Status setMessageLocale( Locale messageLocale );

    public static enum Code {
        SSH_FX_OK(0),
        SSH_FX_EOF(1),
        SSH_FX_NO_SUCH_FILE(2),
        SSH_FX_PERMISSION_DENIED(3),
        SSH_FX_FAILURE(4),
        SSH_FX_BAD_MESSAGE(5),
        SSH_FX_NO_CONNECTION(6),
        SSH_FX_CONNECTION_LOST(7),
        SSH_FX_OP_UNSUPPORTED(8);

        private static Map<Integer, Code> lookup;

        static {
            lookup = new HashMap<>();
            for ( Code code : values() ) {
                lookup.put( code.value, code );
            }
        }

        private int value;

        private Code( int value ) {
            this.value = value;
        }

        public static Code fromValue( int value ) {
            return lookup.get( value );
        }

        public int getValue() {
            return value;
        }
    }
}
