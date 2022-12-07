package com.github.basdxz.vbuffers.accessor;

import java.nio.ByteBuffer;

@FunctionalInterface
public interface Setter<T> {
    void put(ByteBuffer buffer, int offsetBytes, T value);
}
