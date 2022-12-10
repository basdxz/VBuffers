package com.github.basdxz.vbuffers.accessor.io;

import java.nio.ByteBuffer;

public sealed interface Return permits ChainReturn, InReturn, OutReturn, VoidReturn {
    Object handle(Object chainable, ByteBuffer back, int offsetBytes, Object... args);
}
