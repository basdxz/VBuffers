package com.github.basdxz.vbuffers.instance;

import org.jetbrains.annotations.Contract;

import java.util.stream.Stream;

public interface Streamable<LAYOUT extends Streamable<LAYOUT>> extends Buffer<LAYOUT> {
    @Contract(value = "-> new", pure = true)
    Stream<LAYOUT> v$stream();

    @Contract(value = "-> new", pure = true)
    Stream<LAYOUT> v$parallelStream();
}
