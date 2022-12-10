package com.github.basdxz.vbuffers.internal.accessor;

import lombok.*;

import java.nio.ByteBuffer;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
final class ChainReturn implements AccessorReturn {
    @Override
    public Object handle(Object chainable, ByteBuffer back, int offsetBytes, Object... args) {
        return chainable;
    }
}
