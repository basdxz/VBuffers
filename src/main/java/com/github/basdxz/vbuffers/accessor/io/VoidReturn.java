package com.github.basdxz.vbuffers.accessor.io;

import lombok.*;

import java.nio.ByteBuffer;

@NoArgsConstructor
public final class VoidReturn implements Return {
    @Override
    public Object handle(Object thiz, ByteBuffer backingBuffer, int offsetBytes, Object... args) {
        return null;
    }
}
