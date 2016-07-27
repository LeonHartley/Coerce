package io.coerce.networking.channels;

import java.nio.charset.Charset;

public interface NetworkBuffer<T> {
    T getParentBuffer();

    void writeString(final String str);

    void writeInteger(final int i);

    void writeInteger(final int index, final int i);

    void writeBytes(final byte[] bytes);

    void writeBytes(final int index, final byte[] bytes);

    void writeByte(final byte b);

    void writeByte(final int index, final byte b);

    void writeDouble(final double d);

    void writeDouble(final int index, final double d);

    void writeShort(final short s);

    void writeShort(final int index, final short s);

    boolean lengthIsSet();

    boolean setLength();

    String readString();

    int readInteger();

    byte[] readBytes(int length);

    byte readByte();

    double readDouble();

    short readShort();

    String toString(Charset charset);

    int readableBytes();

    void setWriterIndex(final int writerIndex);

    void setReaderIndex(final int readerIndex);

    void markReaderIndex();

    void markWriterIndex();

    void resetReaderIndex();

    void resetWriterIndex();

}
