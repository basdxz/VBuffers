package com.github.basdxz.vbuffers;

import org.jetbrains.annotations.Contract;

import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.stream.Stream;

// TODO: Make tests to make the warnings redundant
@SuppressWarnings({"unused", "UnusedReturnValue"})
public interface VBuffer<LAYOUT extends VBuffer<LAYOUT>> extends Iterable<LAYOUT> {
    String BUFFER_METHOD_PREFIX = "v$";

    @Contract(pure = true)
    ByteBuffer v$backing();

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

    @Contract("_-> this")
    LAYOUT v$decrement(int indexCount);

    @Contract(pure = true)
    int v$limit();

    @Contract("_-> this")
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

    @Contract("_,_,_-> this")
    LAYOUT v$copyStrides(int sourceIndex, int targetIndex, int length);

    @Contract("_,_-> this")
    LAYOUT v$copyStride(int sourceIndex, int targetIndex);

    LAYOUT v$put(LAYOUT source);

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

    @Contract(value = "_,_-> new", pure = true)
    LAYOUT v$sliceView(int startIndex, int length);

    @Contract(value = "-> new", pure = true)
    LAYOUT v$asReadOnlyView();

    @Contract(value = "-> new", pure = true)
    Stream<LAYOUT> v$stream();

    @Contract(value = "-> new", pure = true)
    Stream<LAYOUT> v$parallelStream();

    @Contract(value = "-> new", pure = true)
    Spliterator<LAYOUT> v$spliterator();

    @Contract(value = "-> new", pure = true)
    Iterator<LAYOUT> v$iterator();
}
