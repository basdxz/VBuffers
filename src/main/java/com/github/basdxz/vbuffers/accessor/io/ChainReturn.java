package com.github.basdxz.vbuffers.accessor.io;

import lombok.*;

import java.nio.ByteBuffer;

@AllArgsConstructor
public final class ChainReturn implements Return {
    @Override
    public Object handle(Object chainable, ByteBuffer back, int offsetBytes, Object... args) {
        return chainable;
    }
}
