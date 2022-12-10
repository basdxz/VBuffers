package com.github.basdxz.vbuffers.accessor.back;

import java.nio.ByteBuffer;

@FunctionalInterface
public interface BackingSetter<T> {
    void put(ByteBuffer buffer, int offsetBytes, T value);
}
