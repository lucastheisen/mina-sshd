package org.apache.sshd.sftp.client.packetdata;


import java.util.EnumSet;


import org.apache.sshd.sftp.Request;
import org.apache.sshd.sftp.impl.Maskable;


public interface Open extends BasePath<Open>, BaseAttrs<Open>, Request<Open, Handle> {
    public EnumSet<PFlag> getPFlags();

    public Open setPFlags( EnumSet<PFlag> pflags );

    public enum PFlag implements Maskable<PFlag> {
        SSH_FXF_READ(0x00000001),
        SSH_FXF_WRITE(0x00000002),
        SSH_FXF_APPEND(0x00000004),
        SSH_FXF_CREAT(0x00000008),
        SSH_FXF_TRUNC(0x00000010),
        SSH_FXF_EXCL(0x00000020);

        private int value;

        private PFlag( int value ) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}