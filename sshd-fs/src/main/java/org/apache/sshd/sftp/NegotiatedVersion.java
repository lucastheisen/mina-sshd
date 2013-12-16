package org.apache.sshd.sftp;


import java.util.Map;


public interface NegotiatedVersion {
    public Map<String, String> clientExtensions();

    public Map<String, String> serverExtensions();

    public int version();
}
