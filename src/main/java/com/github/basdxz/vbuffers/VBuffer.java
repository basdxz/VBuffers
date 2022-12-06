package com.github.basdxz.vbuffers;

import org.jetbrains.annotations.Contract;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.stream.Stream;

public interface VBuffer<LAYOUT extends VBuffer<LAYOUT>> extends Iterable<LAYOUT> {
    String BUFFER_METHOD_PREFIX = "v$";

    @Contract(pure = true)
    int v$capacity();

    @Contract(pure = true)
    int v$position();

    @Contract("_-> this")
    LAYOUT v$position(int position);

    @Contract("-> this")
    LAYOUT v$increment();

    @Contract("_-> this")
    LAYOUT v$increment(int indexCount);

    @Contract("-> this")
    LAYOUT v$decrement();

    @Contract("-> this")
    LAYOUT v$decrement(int indexCount);

    @Contract(pure = true)
    int v$limit();

    @Contract("-> this")
    LAYOUT v$limit(int limit);

    @Contract("-> this")
    LAYOUT v$mark();

    @Contract("-> this")
    LAYOUT v$reset();

    @Contract("-> this")
    LAYOUT v$clear();

    @Contract("-> this")
    LAYOUT v$flip();

    @Contract("-> this")
    LAYOUT v$rewind();

    @Contract("-> this")
    LAYOUT v$compact();

    @Contract("___-> this")
    LAYOUT v$copyStride(int sourceIndex, int targetIndex, int length);

    @Contract("__-> this")
    LAYOUT v$copyStride(int sourceIndex, int targetIndex);

    @Contract(pure = true)
    boolean v$hasRemaining();

    @Contract(pure = true)
    int v$remaining();

    @Contract(value = "-> new", pure = true)
    LAYOUT v$duplicateView();

    @Contract(value = "-> new", pure = true)
    LAYOUT v$nextStrideView();

    @Contract(value = "-> new", pure = true)
    LAYOUT v$strideView();

    @Contract(value = "_-> new", pure = true)
    LAYOUT v$strideView(int index);

    @Contract(value = "-> new", pure = true)
    LAYOUT v$sliceView();

    @Contract(value = "__-> new", pure = true)
    LAYOUT v$sliceView(int startIndex, int length);

    @Contract(value = "-> new", pure = true)
    LAYOUT v$asReadOnlyView();

    Stream<LAYOUT> v$stream();

    Stream<LAYOUT> v$parallelStream();

    Spliterator<LAYOUT> v$spliterator();

    Iterator<LAYOUT> v$iterator();
}
