package com.github.basdxz.vbuffers;

import java.nio.ByteBuffer;

@FunctionalInterface
public interface Allocator {
    ByteBuffer allocate(int sizeBytes);
}
