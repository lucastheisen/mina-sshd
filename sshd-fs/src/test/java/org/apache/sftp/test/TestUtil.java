package org.apache.sftp.test;


import java.io.IOException;
import java.util.Properties;


import org.apache.sshd.ClientSession;
import org.apache.sshd.SshClient;


public class TestUtil {
    public static TestUtil instance;
    private static final String KEY_OS_TEMP_DIR = "os.tempdir";
    private static final String KEY_SSH_HOSTNAME = "ssh.hostname";
    private static final String KEY_SSH_PASSWORD = "ssh.password";
    private static final String KEY_SSH_PORT = "ssh.port";
    private static final String KEY_SSH_TEMP_DIR = "ssh.tempdir";
    private static final String KEY_SSH_USERNAME = "ssh.username";

    private Properties properties;

    private TestUtil() throws IOException {
        properties = new Properties();
        properties.load( getClass().getClassLoader().getResourceAsStream( "configuration.properties" ) );
    }

    public static TestUtil instance() throws IOException {
        if ( instance == null ) {
            instance = new TestUtil();
        }
        return instance;
    }

    public ClientSession newSession() throws InterruptedException, IOException {
        String hostname = properties.getProperty( KEY_SSH_HOSTNAME );
        final int port = Integer.parseInt( properties.getProperty( KEY_SSH_PORT ) );
        final String username = properties.getProperty( KEY_SSH_USERNAME );
        final String password = properties.getProperty( KEY_SSH_PASSWORD );

        SshClient client = SshClient.setUpDefaultClient();
        client.start();
        ClientSession session = client.connect( hostname, port ).await().getSession();
        session.authPassword( username, password ).await();
        return session;
    }

    public String osTempDir() {
        return properties.getProperty( KEY_OS_TEMP_DIR );
    }

    public String sshTempDir() {
        return properties.getProperty( KEY_SSH_TEMP_DIR );
    }
}
