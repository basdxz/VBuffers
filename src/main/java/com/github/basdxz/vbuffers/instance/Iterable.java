package com.github.basdxz.vbuffers.instance;

import org.jetbrains.annotations.Contract;

import java.util.Iterator;
import java.util.Spliterator;

public interface Iterable<LAYOUT extends Iterable<LAYOUT>> extends Buffer<LAYOUT>, java.lang.Iterable<LAYOUT> {
    @Contract(value = "-> new", pure = true)
    Iterator<LAYOUT> v$iterator();

    @Contract(value = "-> new", pure = true)
    Spliterator<LAYOUT> v$spliterator();
}
