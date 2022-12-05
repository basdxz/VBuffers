package com.github.basdxz.vbuffers;

public interface VBuffer {
    int capacity();

    int position();

    void position(int position);

    int limit();

    void limit(int limit);

    void mark();

    void reset();

    void clear();

    void flip();

    void rewind();

    void compact();

    void copy(int sourceIndex, int targetIndex, int length);

    void copy(int sourceIndex, int targetIndex);

    void next();

    boolean hasRemaining();

    int remaining();
}
