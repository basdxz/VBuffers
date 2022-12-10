package com.github.basdxz.vbuffers.accessor.io;

import lombok.*;

@AllArgsConstructor
public final class IdxBinding {
    private final int argumentIndex;
    private final int strideBytes;

    public int strideOffset(Object... args) {
        return (int) args[argumentIndex] * strideBytes;
    }
}
