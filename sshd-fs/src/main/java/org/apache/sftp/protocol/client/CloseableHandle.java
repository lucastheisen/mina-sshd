package org.apache.sftp.protocol.client;


import java.io.Closeable;


import org.apache.sftp.protocol.packetdata.Handle;


/**
 * Decorator for Handle that adds Closeable to allow for use in
 * try-with-resources.
 * 
 * @author Lucas Theisen
 */
public interface CloseableHandle extends Handle, Closeable {
}
