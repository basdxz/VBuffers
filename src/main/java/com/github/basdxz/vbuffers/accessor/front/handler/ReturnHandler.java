package com.github.basdxz.vbuffers.accessor.front.handler;

import java.nio.ByteBuffer;

public interface ReturnHandler {
    Object handle(Object chainable, ByteBuffer back, int offsetBytes, Object... args);
}
