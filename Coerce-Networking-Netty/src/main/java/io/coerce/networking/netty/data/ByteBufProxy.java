package io.coerce.networking.netty.data;

import io.coerce.networking.channels.NetworkBuffer;
import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;

public class ByteBufProxy implements NetworkBuffer<ByteBuf> {

    private ByteBuf buffer;

    private int lengthIndex = 0;
    private boolean lengthIsSet = false;

    public ByteBufProxy(ByteBuf byteBuf) {
        this.buffer = byteBuf;

        this.lengthIndex = this.buffer.writerIndex();
    }

    @Override
    public ByteBuf getParentBuffer() {
        return this.buffer;
    }

    @Override
    public void writeString(String str) {
        this.buffer.writeShort(str.length());
        this.buffer.writeBytes(str.getBytes(CharsetUtil.UTF_8));
    }

    @Override
    public void writeInteger(int i) {
        this.buffer.writeInt(i);
    }

    @Override
    public void writeInteger(int index, int i) {
        this.buffer.setInt(index, i);
    }

    @Override
    public void writeBytes(byte[] bytes) {
        this.buffer.writeBytes(bytes);
    }

    @Override
    public void writeBytes(int index, byte[] bytes) {

    }

    @Override
    public void writeByte(byte b) {
        this.buffer.writeByte(b);
    }

    @Override
    public void writeByte(int index, byte b) {

    }

    @Override
    public void writeDouble(double d) {
        this.buffer.writeDouble(d);
    }

    @Override
    public void writeDouble(int index, double d) {

    }

    @Override
    public void writeShort(short s) {
        this.buffer.writeShort(s);
    }

    @Override
    public void writeShort(int index, short s) {

    }

    @Override
    public boolean lengthIsSet() {
        return this.lengthIsSet;
    }

    @Override
    public boolean setLength() {
        final int length = this.buffer.writerIndex() - 4;

        System.out.println("Writing length: " + length);

        this.writeInteger(this.lengthIndex, length);

        return false;
    }

    @Override
    public String readString() {
        int length = this.readShort();

        return this.buffer.readBytes(length).toString(CharsetUtil.UTF_8);
    }

    @Override
    public int readInteger() {
        return this.buffer.readInt();
    }

    @Override
    public byte[] readBytes(int length) {
        final byte[] bytes = new byte[length];

        for (int i = 0; i < length; i++) {
            bytes[i] = this.readByte();
        }

        return bytes;
    }

    @Override
    public byte readByte() {
        return this.buffer.readByte();
    }

    @Override
    public double readDouble() {
        return this.buffer.readDouble();
    }

    @Override
    public short readShort() {
        return this.buffer.readShort();
    }

    @Override
    public String toString(Charset charset) {
        return this.buffer.toString(charset);
    }

    @Override
    public int readableBytes() {
        return this.buffer.readableBytes();
    }

    @Override
    public void setWriterIndex(int writerIndex) {
        this.buffer.writerIndex(writerIndex);
    }

    @Override
    public void setReaderIndex(int readerIndex) {
        this.buffer.readerIndex(readerIndex);
    }

    @Override
    public void markReaderIndex() {
        this.buffer.markReaderIndex();
    }

    @Override
    public void markWriterIndex() {
        this.buffer.markWriterIndex();
    }

    @Override
    public void resetReaderIndex() {
        this.buffer.resetReaderIndex();
    }

    @Override
    public void resetWriterIndex() {
        this.buffer.resetWriterIndex();
    }

    public void dispose() {
        this.buffer = null;
    }
}
