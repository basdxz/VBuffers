package com.github.basdxz.vbuffers;

import java.nio.ByteBuffer;

@FunctionalInterface
public interface BackingProvider {
    ByteBuffer newBacking(int sizeBytes);
}
