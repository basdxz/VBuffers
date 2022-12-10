package com.github.basdxz.vbuffers.accessor.io;

import java.nio.ByteBuffer;

public sealed interface Return permits ThisReturn, InReturn, OutReturn, VoidReturn {
    Object handle(Object thiz, ByteBuffer backingBuffer, int offsetBytes, Object... args);
}
