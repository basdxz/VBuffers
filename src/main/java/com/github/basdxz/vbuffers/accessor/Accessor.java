package com.github.basdxz.vbuffers.accessor;

import java.nio.ByteBuffer;

public interface Accessor {
    Object access(Object chainable, ByteBuffer back, int offsetBytes, Object... args);

    boolean writing();
}
