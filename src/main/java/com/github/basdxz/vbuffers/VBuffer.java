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

    boolean hasRemaining();

    int remaining();
}
