package com.github.basdxz.vbuffers.buffer;

import org.jetbrains.annotations.Contract;

import java.util.stream.Stream;

public interface StreamableBuffer<LAYOUT extends StreamableBuffer<LAYOUT>> extends Buffer<LAYOUT> {
    @Contract(value = "-> new", pure = true)
    Stream<LAYOUT> v$stream();

    @Contract(value = "-> new", pure = true)
    Stream<LAYOUT> v$parallelStream();
}
