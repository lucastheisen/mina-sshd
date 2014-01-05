package org.apache.sftp.protocol.packetdata.openssh.impl;


import java.util.EnumSet;


import org.apache.sftp.protocol.PacketType;
import org.apache.sftp.protocol.impl.MaskFactory;
import org.apache.sftp.protocol.impl.SftpProtocolBuffer;
import org.apache.sftp.protocol.packetdata.impl.AbstractPacketData;
import org.apache.sftp.protocol.packetdata.openssh.StatVfsReply;


public class DefaultStatVfsReply extends AbstractPacketData<StatVfsReply> implements StatVfsReply {
    private long blocksAvailable;
    private long blocksFree;
    private long blockSize;
    private long filesAvailable;
    private long filesFree;
    private long fileSid;
    private EnumSet<Flag> flags;
    private long fundamentalBlockSize;
    private long nameMaxLength;
    private long totalBlocks;
    private long totalFiles;

    @Override
    public long getBlocksAvailable() {
        return blocksAvailable;
    }

    @Override
    public long getBlocksFree() {
        return blocksFree;
    }

    @Override
    public long getBlockSize() {
        return blockSize;
    }

    @Override
    public long getFilesAvailable() {
        return filesAvailable;
    }

    @Override
    public long getFilesFree() {
        return filesFree;
    }

    @Override
    public long getFileSid() {
        return fileSid;
    }

    @Override
    public EnumSet<Flag> getFlags() {
        return flags;
    }

    @Override
    public long getFundamentalBlockSize() {
        return fundamentalBlockSize;
    }

    @Override
    public long getNameMaxLength() {
        return nameMaxLength;
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.SSH_FXP_EXTENDED_REPLY;
    }

    @Override
    public long getTotalBlocks() {
        return totalBlocks;
    }

    @Override
    public long getTotalFiles() {
        return totalFiles;
    }

    @Override
    public StatVfsReply parseFrom( SftpProtocolBuffer buffer ) {
        blockSize = buffer.getLong();
        fundamentalBlockSize = buffer.getLong();
        totalBlocks = buffer.getLong();
        blocksFree = buffer.getLong();
        blocksAvailable = buffer.getLong();
        totalFiles = buffer.getLong();
        filesFree = buffer.getLong();
        filesAvailable = buffer.getLong();
        fileSid = buffer.getLong();
        flags = MaskFactory.fromMask( buffer.getLong(), Flag.class );
        nameMaxLength = buffer.getLong();
        return this;
    }

    @Override
    public void setBlocksAvailable( long blocksAvailable ) {
        this.blocksAvailable = blocksAvailable;
    }

    @Override
    public void setBlocksFree( long blocksFree ) {
        this.blocksFree = blocksFree;
    }

    @Override
    public void setBlockSize( long blockSize ) {
        this.blockSize = blockSize;
    }

    @Override
    public void setFilesAvailable( long filesAvailable ) {
        this.filesAvailable = filesAvailable;
    }

    @Override
    public void setFilesFree( long filesFree ) {
        this.filesFree = filesFree;
    }

    @Override
    public void setFileSid( long fileSid ) {
        this.fileSid = fileSid;
    }

    @Override
    public void setFlags( EnumSet<Flag> flags ) {
        this.flags = flags;
    }

    @Override
    public void setFundamentalBlockSize( long fundamentalBlockSize ) {
        this.fundamentalBlockSize = fundamentalBlockSize;
    }

    @Override
    public void setNameMaxLength( long nameMaxLength ) {
        this.nameMaxLength = nameMaxLength;
    }

    @Override
    public void setTotalBlocks( long totalBlocks ) {
        this.totalBlocks = totalBlocks;
    }

    @Override
    public void setTotalFiles( long totalFiles ) {
        this.totalFiles = totalFiles;
    }

    @Override
    public void writeTo( SftpProtocolBuffer buffer ) {
        buffer.putLong( blockSize );
        buffer.putLong( fundamentalBlockSize );
        buffer.putLong( totalBlocks );
        buffer.putLong( blocksFree );
        buffer.putLong( blocksAvailable );
        buffer.putLong( totalFiles );
        buffer.putLong( filesFree );
        buffer.putLong( filesAvailable );
        buffer.putLong( fileSid );
        buffer.putLong( MaskFactory.toMask( flags ) );
        buffer.putLong( nameMaxLength );
    }
}
