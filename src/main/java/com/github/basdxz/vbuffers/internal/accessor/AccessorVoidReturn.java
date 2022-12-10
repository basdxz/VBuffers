package com.github.basdxz.vbuffers.internal.accessor;

import lombok.*;

import java.nio.ByteBuffer;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
final class AccessorVoidReturn implements AccessorReturn {
    @Override
    public Object handle(Object chainable, ByteBuffer back, int offsetBytes, Object... args) {
        return null;
    }
}
