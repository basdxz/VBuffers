package com.github.basdxz.vbuffers;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.stream.Stream;

public interface VBuffer<LAYOUT extends VBuffer<LAYOUT>> extends Iterable<LAYOUT> {
    String BUFFER_METHOD_PREFIX = "v$";

    int v$capacity();

    int v$position();

    LAYOUT v$position(int position);

    LAYOUT v$increment();

    LAYOUT v$increment(int indexCount);

    LAYOUT v$decrement();

    LAYOUT v$decrement(int indexCount);

    int v$limit();

    LAYOUT v$limit(int limit);

    LAYOUT v$mark();

    LAYOUT v$reset();

    LAYOUT v$clear();

    LAYOUT v$flip();

    LAYOUT v$rewind();

    LAYOUT v$compact();

    LAYOUT v$copyStride(int sourceIndex, int targetIndex, int length);

    LAYOUT v$copyStride(int sourceIndex, int targetIndex);

    boolean v$hasRemaining();

    int v$remaining();

    // These return NEW Layout obj
    LAYOUT v$duplicateView();

    LAYOUT v$nextStrideView();

    LAYOUT v$strideView();

    LAYOUT v$strideView(int index);

    LAYOUT v$sliceView();

    LAYOUT v$sliceView(int startIndex, int length);

    LAYOUT v$asReadOnlyView();

    Stream<LAYOUT> v$stream();

    Stream<LAYOUT> v$parallelStream();

    Spliterator<LAYOUT> v$spliterator();

    Iterator<LAYOUT> v$iterator();
}
