package com.github.basdxz.vbuffers.access.front;

import java.nio.ByteBuffer;

public interface ReturnHandler {
    Object handle(Object chainable, ByteBuffer back, int offsetBytes, Object... args);
}
