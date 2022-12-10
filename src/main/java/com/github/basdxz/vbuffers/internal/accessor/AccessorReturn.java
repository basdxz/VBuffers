package com.github.basdxz.vbuffers.internal.accessor;

import java.nio.ByteBuffer;

interface AccessorReturn {
    Object handle(Object chainable, ByteBuffer back, int offsetBytes, Object... args);
}
