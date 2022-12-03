package com.github.basdxz.vbuffers;

import java.nio.ByteBuffer;

@FunctionalInterface
public interface Allocator {
    ByteBuffer newBacking(int sizeBytes);
}
