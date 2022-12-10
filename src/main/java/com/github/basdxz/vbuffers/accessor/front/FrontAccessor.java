package com.github.basdxz.vbuffers.accessor.front;

import java.nio.ByteBuffer;

public interface FrontAccessor {
    Object access(Object chainable, ByteBuffer back, int offsetBytes, Object... args);

    boolean writing();
}
