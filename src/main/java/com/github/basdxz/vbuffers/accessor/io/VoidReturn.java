package com.github.basdxz.vbuffers.accessor.io;

import lombok.*;

import java.nio.ByteBuffer;

@NoArgsConstructor
public final class VoidReturn implements Return {
    @Override
    public Object handle(Object chainable, ByteBuffer back, int offsetBytes, Object... args) {
        return null;
    }
}
