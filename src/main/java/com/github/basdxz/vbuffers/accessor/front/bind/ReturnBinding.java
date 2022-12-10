package com.github.basdxz.vbuffers.accessor.front.bind;

import java.nio.ByteBuffer;

public interface ReturnBinding {
    Object handle(Object chainable, ByteBuffer back, int offsetBytes, Object... args);
}
