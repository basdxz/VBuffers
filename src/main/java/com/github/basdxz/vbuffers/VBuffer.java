package com.github.basdxz.vbuffers;

public interface VBuffer<LAYOUT extends VBuffer<LAYOUT>> {
    String BUFFER_METHOD_PREFIX = "v$";

    int v$capacity();

    int v$position();

    void v$position(int position);

    int v$limit();

    void v$limit(int limit);

    void v$mark();

    void v$reset();

    void v$clear();

    void v$flip();

    void v$rewind();

    void v$compact();

    void v$copy(int sourceIndex, int targetIndex, int length);

    void v$copy(int sourceIndex, int targetIndex);

    boolean v$next();

    boolean v$hasRemaining();

    int v$remaining();

    LAYOUT v$duplicate();
}
