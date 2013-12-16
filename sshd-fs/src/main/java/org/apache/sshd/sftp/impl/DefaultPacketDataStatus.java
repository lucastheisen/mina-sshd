package org.apache.sshd.sftp.impl;


import java.util.Locale;


import org.apache.sshd.sftp.PacketType;
import org.apache.sshd.sftp.client.packetdata.Status;


public class DefaultPacketDataStatus
        extends AbstractRequestOrResponse<Status>
        implements Status {
    private Code code;
    private String message;
    private Locale messageLocale;

    @Override
    public void appendToStringBuilder( StringBuilder builder ) {
        builder.append( "'code':'" ).append( code.name() )
                .append( "','messageLocale':'" ).append( messageLocale.toString() )
                .append( "','message':'" ).append( message ).append( "'" );
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.SSH_FXP_STATUS;
    }

    @Override
    public Code getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Locale getMessageLocale() {
        return messageLocale;
    }

    @Override
    public DefaultPacketDataStatus parseRequestFrom( SftpProtocolBuffer buffer ) {
        code = Code.fromValue( buffer.getInt() );
        message = buffer.getString();
        messageLocale = Locale.forLanguageTag( buffer.getString() );
        return this;
    }

    @Override
    public DefaultPacketDataStatus setCode( Code code ) {
        this.code = code;
        return this;
    }

    @Override
    public DefaultPacketDataStatus setMessageLocale( Locale messageLocale ) {
        this.messageLocale = messageLocale;
        return this;
    }

    @Override
    public DefaultPacketDataStatus setMessage( String message ) {
        this.message = message;
        return this;
    }

    @Override
    public void writeRequestTo( SftpProtocolBuffer buffer ) {
        buffer.putInt( code.getValue() );
        buffer.putString( message );
        buffer.putString( messageLocale.toLanguageTag() );
    }
}