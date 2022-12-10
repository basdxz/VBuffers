package com.github.basdxz.vbuffers.accessor.io;

import lombok.*;

import java.nio.ByteBuffer;

@AllArgsConstructor
public final class ThisReturn implements Return {
    @Override
    public Object handle(Object thiz, ByteBuffer backingBuffer, int offsetBytes, Object... args) {
        return thiz;
    }
}
