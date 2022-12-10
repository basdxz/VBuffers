package com.github.basdxz.vbuffers.buffer;

import org.jetbrains.annotations.Contract;

import java.util.Iterator;
import java.util.Spliterator;

public interface IterableBuffer<LAYOUT extends IterableBuffer<LAYOUT>> extends Buffer<LAYOUT>, Iterable<LAYOUT> {
    @Contract(value = "-> new", pure = true)
    Iterator<LAYOUT> v$iterator();

    @Contract(value = "-> new", pure = true)
    Spliterator<LAYOUT> v$spliterator();
}
