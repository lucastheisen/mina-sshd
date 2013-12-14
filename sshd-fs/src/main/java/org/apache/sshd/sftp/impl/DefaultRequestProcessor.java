package org.apache.sshd.sftp.impl;


import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


import org.apache.sshd.ClientSession;
import org.apache.sshd.client.channel.ChannelSubsystem;
import org.apache.sshd.sftp.PacketData;
import org.apache.sshd.sftp.PacketDataFactory;
import org.apache.sshd.sftp.PacketType;
import org.apache.sshd.sftp.Request;
import org.apache.sshd.sftp.RequestProcessor;
import org.apache.sshd.sftp.Response;
import org.apache.sshd.sftp.client.packetdata.Init;
import org.apache.sshd.sftp.client.packetdata.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DefaultRequestProcessor implements RequestProcessor {
    private static Logger logger = LoggerFactory.getLogger( DefaultRequestProcessor.class );
    private static final int CLIENT_SFTP_PROTOCOL_VERSION = 3;

    private ClientSession clientSession;
    private Init init;
    private PacketDataFactory packetDataFactory;
    private ResponseProcessor responseProcessor;
    private ChannelSubsystem sftpChannel;
    private Version version = null;
    private SftpProtocolBuffer writeBuffer = SftpProtocolBuffer.allocateDirect( 65536 );
    private ReentrantLock writeLock = new ReentrantLock();

    public DefaultRequestProcessor( ClientSession clientSession, PacketDataFactory packetDataFactory )
            throws IOException, InterruptedException, ExecutionException {
        this.clientSession = clientSession;
        this.packetDataFactory = packetDataFactory;
        this.sftpChannel = clientSession.createSubsystemChannel( "sftp" );
        ResponseProcessor responseProcessor = new ResponseProcessor();
        try {
            this.sftpChannel.setOut( responseProcessor );
            this.sftpChannel.setErr( new OutputStream() {
                @Override
                public void write( int b ) throws IOException {
                    // TODO determine what should be done with writes to err
                }
            } );
            this.sftpChannel.open().await();
            logger.info( "sftp channel open" );
        }
        catch ( InterruptedException e ) {
            throw (IOException)new InterruptedIOException().initCause( e );
        }

        init = ((Init)packetDataFactory.newInstance( PacketType.SSH_FXP_INIT ))
                .setVersion( CLIENT_SFTP_PROTOCOL_VERSION );
        writePacket( init );
        this.version = responseProcessor.getVersion().get();
    }

    @Override
    public void close() throws IOException {
        clientSession.close( true );
    }

    public void close( boolean immediately ) {
        clientSession.close( immediately );
    }

//    void resizeBuffer( int atLeast ) throws IOException {
//        int newSize = writeBuffer.capacity() + Math.max( atLeast, writeBuffer.capacity() );
//        ByteBuffer newWriteBuffer = ByteBuffer.allocateDirect( newSize );
//        writeBuffer.flip();
//        newWriteBuffer.put( writeBuffer );
//        writeBuffer = newWriteBuffer;
//    }

    public int negotiatedVersion() {
        return Math.min( init.getVersion(), version.getVersion() );
    }

    private void writePacket( PacketData packetData ) throws IOException {
        int packetDataSize = packetData.getSize();
        int packetSize = packetDataSize + 5;
        try {
            writeLock.lock();
            // ensure size
            writeBuffer.ensureSize( packetSize );
            writeBuffer.putInt( packetDataSize + 1 );
            writeBuffer.putPacketType( packetData.getPacketType() );
            packetData.writeTo( writeBuffer );
            writeBuffer.flip();

            if ( logger.isTraceEnabled() ) {
                byte[] traceBytes = new byte[writeBuffer.remaining()];
                writeBuffer.get( traceBytes );
                logger.trace( "writeBuffer={}", traceBytes );
                writeBuffer.flip();
            }

            Channels.newChannel( this.sftpChannel.getInvertedIn() )
                    .write( writeBuffer.getByteBuffer() );

            this.sftpChannel.getInvertedIn().flush();

            writeBuffer.clear();
        }
        finally {
            writeLock.unlock();
        }
    }

    @Override
    public Future<Response> request( Request request ) throws IOException {
        FuturePacketData<Response> response = responseProcessor.getFutureResponse( request );
        writePacket( request );
        return response;
    }

    private static class FuturePacketData<T extends PacketData> implements Future<T> {
        private Condition condition;
        private Lock lock;
        private T packetData;

        private FuturePacketData( Lock lock ) {
            this.lock = lock;
            this.condition = lock.newCondition();
        }

        @Override
        public boolean cancel( boolean mayInterruptIfRunning ) {
            // TODO implement cancel
            throw new UnsupportedOperationException( "not yet implemented" );
        }

        @Override
        public boolean isCancelled() {
            // TODO implement is cancelled
            throw new UnsupportedOperationException( "not yet implemented" );
        }

        @Override
        public boolean isDone() {
            return packetData != null;
        }

        @Override
        public T get() throws InterruptedException, ExecutionException {
            if ( packetData == null ) {
                try {
                    lock.lock();
                    condition.await();
                }
                finally {
                    lock.unlock();
                }
            }
            return packetData;
        }

        @Override
        public T get( long timeout, TimeUnit unit ) throws InterruptedException, ExecutionException, TimeoutException {
            if ( packetData == null ) {
                try {
                    lock.lock();
                    condition.await( timeout, unit );
                }
                finally {
                    lock.unlock();
                }
            }
            return packetData;
        }

        private void setPacketData( T packetData ) {
            this.packetData = packetData;
            try {
                lock.lock();
                condition.signalAll();
            }
            finally {
                lock.unlock();
            }
        }
    }

    private class ResponseProcessor extends OutputStream {
        private ReentrantLock futureLock = new ReentrantLock();
        private FuturePacketData<Version> futureVersion;
        private ConcurrentHashMap<Integer, FuturePacketData<Response>> responses;
        private ByteBuffer readBuffer = ByteBuffer.allocateDirect( 65536 );
        private int currentPacketStart = readBuffer.position();
        private int currentPacketEnd = -1;

        public ResponseProcessor() {
            futureVersion = new FuturePacketData<>( futureLock );
        }

        public FuturePacketData<Response> getFutureResponse( Request request ) {
            FuturePacketData<Response> response = new FuturePacketData<>( futureLock );
            responses.put( request.getId(), response );
            return response;
        }

        public Future<Version> getVersion() {
            return futureVersion;
        }

        void processWrite() throws IOException {
            int position = readBuffer.position();
            if ( currentPacketEnd < 0 && position >= currentPacketStart + 4 ) {
                currentPacketEnd = currentPacketStart + 4 + readBuffer.getInt( currentPacketStart );
            }
            if ( position >= currentPacketEnd ) {
                // got one
                readBuffer.position( currentPacketStart );
                readBuffer.limit( currentPacketEnd );
                processPacket( SftpProtocolBuffer.wrap( readBuffer.asReadOnlyBuffer() ) );

                // clean up and prepare for more
                readBuffer.compact();
                currentPacketStart = readBuffer.position();
                currentPacketEnd = -1;
            }
        }

        void processPacket( SftpProtocolBuffer buffer ) {
            buffer.getInt(); // packet size not currently used
            PacketType packetType = buffer.getPacketType();
            PacketData packetData = packetDataFactory.newInstance( packetType )
                    .parseFrom( buffer );
            if ( packetType == PacketType.SSH_FXP_VERSION ) {
                futureVersion.setPacketData( (Version)packetData );
            }
            else if ( packetData instanceof Response ) {
                Response response = ((Response)packetData);
                FuturePacketData<Response> futureResponse = responses.remove( response.getId() );
                if ( futureResponse != null ) {
                    futureResponse.setPacketData( response );
                }
            }
            else {
                throw new UnsupportedOperationException( "unexpected packet: " + packetType );
            }
        }

        void resizeBuffer( int atLeast ) throws IOException {
            int newSize = readBuffer.capacity() + Math.max( atLeast, readBuffer.capacity() );
            ByteBuffer newReadBuffer = ByteBuffer.allocateDirect( newSize );
            readBuffer.flip();
            newReadBuffer.put( readBuffer );
            readBuffer = newReadBuffer;
        }

        @Override
        public void write( int b ) throws IOException {
            if ( readBuffer.remaining() < 1 ) {
                resizeBuffer( 1 );
            }
            readBuffer.put( (byte)b );
            processWrite();
        }

        @Override
        public void write( byte[] b, int off, int len ) throws IOException {
            if ( readBuffer.remaining() < len ) {
                resizeBuffer( len );
            }
            readBuffer.put( b, off, len );
            processWrite();
        }
    }
}
