package com.github.basdxz.vbuffers.binding;

import java.nio.ByteBuffer;

@FunctionalInterface
public interface SetterBinding<T> {
    void put(ByteBuffer buffer, int offsetBytes, T value);
}
