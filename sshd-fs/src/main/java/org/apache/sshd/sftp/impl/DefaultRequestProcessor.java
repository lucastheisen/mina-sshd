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
import org.apache.sshd.sftp.NegotiatedVersion;
import org.apache.sshd.sftp.PacketData;
import org.apache.sshd.sftp.PacketDataFactory;
import org.apache.sshd.sftp.Request;
import org.apache.sshd.sftp.RequestIdFactory;
import org.apache.sshd.sftp.RequestProcessor;
import org.apache.sshd.sftp.Response;
import org.apache.sshd.sftp.StatusException;
import org.apache.sshd.sftp.UnexpectedPacketDataException;
import org.apache.sshd.sftp.client.packetdata.Init;
import org.apache.sshd.sftp.client.packetdata.Status;
import org.apache.sshd.sftp.client.packetdata.Version;
import org.apache.sshd.sftp.client.packetdata.impl.DefaultPacketDataFactory;
import org.apache.sshd.sftp.impl.SftpProtocolBuffer.Placeholder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DefaultRequestProcessor implements RequestProcessor {
    private static Logger logger = LoggerFactory.getLogger( DefaultRequestProcessor.class );
    private static final int CLIENT_SFTP_PROTOCOL_VERSION = 3;

    private PacketDataFactory packetDataFactory;
    private RequestIdFactory requestIdFactory;
    private ResponseProcessor responseProcessor;
    private ChannelSubsystem sftpChannel;
    private NegotiatedVersion version;
    private SftpProtocolBuffer writeBuffer = SftpProtocolBuffer.allocateDirect( 65536 );
    private ReentrantLock writeLock = new ReentrantLock();

    public DefaultRequestProcessor( ClientSession clientSession )
            throws IOException, InterruptedException, ExecutionException {
        this( clientSession, new DefaultPacketDataFactory() );
    }

    public DefaultRequestProcessor( ClientSession clientSession, PacketDataFactory packetDataFactory )
            throws IOException, InterruptedException, ExecutionException {
        this( clientSession, packetDataFactory, new DefaultRequestIdFactory() );
    }

    public DefaultRequestProcessor( ClientSession clientSession,
            PacketDataFactory packetDataFactory, RequestIdFactory requestIdFactory )
            throws IOException, InterruptedException, ExecutionException {
        this.packetDataFactory = packetDataFactory;
        this.requestIdFactory = requestIdFactory;
        this.responseProcessor = new ResponseProcessor();

        // initialize the sftp channel
        this.sftpChannel = clientSession.createSubsystemChannel( "sftp" );
        try {
            this.sftpChannel.setOut( responseProcessor );
            this.sftpChannel.setErr( new OutputStream() {
                @Override
                public void write( int b ) throws IOException {
                    logger.error( "STDERR: {}", b );
                }
            } );
            this.sftpChannel.open().await();
            logger.info( "sftp channel open" );
        }
        catch ( InterruptedException e ) {
            throw (IOException)new InterruptedIOException().initCause( e );
        }

        // initialized the protocol
        Init init = packetDataFactory.newInstance( Init.class )
                .setVersion( CLIENT_SFTP_PROTOCOL_VERSION );
        writeInit( init );
        Version version = responseProcessor.getVersion().get();
        this.version = new DefaultNegotiatedVersion( init, version );
        logger.info( "Server version: {}", this.version );
    }

    @Override
    public void close() throws IOException {
        sftpChannel.close( true );
    }

    public void close( boolean immediately ) {
        sftpChannel.close( immediately );
    }

    public NegotiatedVersion negotiatedVersion() {
        return version;
    }

    @Override
    public <S extends Response<S>, T extends Request<T, S>> T newRequest( Class<T> type ) {
        return packetDataFactory.newInstance( type );
    }

    @Override
    public <S extends Response<S>, T extends Request<T, S>> Future<S> request( Request<T, S> request )
            throws IOException {
        int requestId = requestIdFactory.nextId();
        Future<S> response =
                responseProcessor.getFutureResponse( requestId, request.expectedResponseType() );
        logger.debug( "sending {}", request );
        writeRequest( request, requestId );
        return response;
    }

    private <S extends Response<S>, T extends Request<T, S>> void writeRequest(
            Request<T, S> request, int requestId ) throws IOException {
        try {
            writeLock.lock();
            Placeholder<Void> size = writeBuffer.newSizePlaceholder();
            writeBuffer.put( size )
                    .put( request.getPacketTypeByte() )
                    .putInt( requestId );
            request.writeTo( writeBuffer );
            size.setValue( null );

            writePacket();
        }
        finally {
            writeLock.unlock();
        }
    }

    private void writeInit( Init init ) throws IOException {
        try {
            writeLock.lock();
            Placeholder<Void> size = writeBuffer.newSizePlaceholder();
            writeBuffer.put( size )
                    .put( init.getPacketTypeByte() );
            init.writeTo( writeBuffer );
            size.setValue( null );

            writePacket();
        }
        finally {
            writeLock.unlock();
        }
    }

    private void writePacket() throws IOException {
        if ( logger.isTraceEnabled() ) {
            writeBuffer.flip();
            byte[] traceBytes = new byte[writeBuffer.remaining()];
            writeBuffer.get( traceBytes );
            logger.trace( "writeBuffer={}", traceBytes );
        }

        writeBuffer.flip();
        Channels.newChannel( this.sftpChannel.getInvertedIn() )
                .write( writeBuffer.getByteBuffer() );

        this.sftpChannel.getInvertedIn().flush();

        writeBuffer.clear();
    }

    public static class FuturePacketData<T extends PacketData<T>> implements Future<T> {
        private Condition condition;
        private boolean done = false;
        private ExecutionException executionException;
        private Class<T> expectedType;
        private Lock lock;
        private T packetData;

        private FuturePacketData( Class<T> expectedType, Lock lock ) {
            this.expectedType = expectedType;
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
            return done;
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
            if ( executionException != null ) {
                throw executionException;
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
            if ( executionException != null ) {
                throw executionException;
            }
            return packetData;
        }

        private void setPacketData( PacketData<?> packetData ) {
            if ( expectedType.isAssignableFrom( packetData.getClass() ) ) {
                this.packetData = expectedType.cast( packetData );
            }
            else if ( Status.class.isAssignableFrom( packetData.getClass() ) ) {
                executionException = new ExecutionException(
                        new StatusException( (Status)packetData ) );
            }
            else {
                executionException = new ExecutionException(
                        new UnexpectedPacketDataException( packetData ) );
            }
            done = true;

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
        private ConcurrentHashMap<Integer, FuturePacketData<? extends PacketData<?>>> responses;
        private ByteBuffer readBuffer = ByteBuffer.allocateDirect( 65536 );
        private int currentPacketStart = readBuffer.position();
        private int currentPacketEnd = -1;

        public ResponseProcessor() {
            futureVersion = new FuturePacketData<>( Version.class, futureLock );
            responses = new ConcurrentHashMap<>();
        }

        public <S extends Response<S>> Future<S> getFutureResponse( int requestId, Class<S> responseType ) {
            FuturePacketData<S> response = new FuturePacketData<>( responseType, futureLock );
            responses.put( requestId, response );
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
            // packet size not currently used as buffer is already set with the
            // appropriate remaining()
            buffer.getInt();

            PacketData<?> packetData = packetDataFactory.newInstance( buffer.get() );
            if ( packetData instanceof Version ) {
                packetData.parseFrom( buffer );
                futureVersion.setPacketData( (Version)packetData );
            }
            else if ( packetData instanceof Response ) {
                int requestId = buffer.getInt();
                packetData.parseFrom( buffer );
                logger.debug( "recieved {}", packetData );
                FuturePacketData<?> futureResponse = responses.remove( requestId );
                if ( futureResponse != null ) {
                    futureResponse.setPacketData( packetData );
                }
            }
            else {
                throw new UnsupportedOperationException( "unexpected packet: " + packetData.toString() );
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
