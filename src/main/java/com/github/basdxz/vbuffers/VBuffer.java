package com.github.basdxz.vbuffers;

public interface VBuffer<LAYOUT extends VBuffer<LAYOUT>> {
    String BUFFER_METHOD_PREFIX = "v$";

    int v$capacity();

    int v$position();

    // TODO: make throw
    LAYOUT v$position(int position);

    int v$limit();

    // TODO: make throw
    LAYOUT v$limit(int limit);

    LAYOUT v$mark();

    LAYOUT v$reset();

    LAYOUT v$clear();

    LAYOUT v$flip();

    LAYOUT v$rewind();

    LAYOUT v$compact();

    LAYOUT v$copy(int sourceIndex, int targetIndex, int length);

    LAYOUT v$copy(int sourceIndex, int targetIndex);

    //TODO: make throw
    LAYOUT v$next();

    boolean v$hasRemaining();

    int v$remaining();

    LAYOUT v$duplicate();

    LAYOUT v$slice();

    LAYOUT v$asReadOnly();
}
