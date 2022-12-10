package com.github.basdxz.vbuffers.accessor;

import java.nio.ByteBuffer;

public interface Accessor {
    Object access(Object thiz, ByteBuffer backingBuffer, int offsetBytes, Object... args);

    boolean writing();
}
