package com.github.basdxz.vbuffers.internal.accessor;

import lombok.*;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
final class AccessorIdxBinding {
    private final int argumentIndex;
    private final int strideBytes;

    public int strideOffset(Object... args) {
        return (int) args[argumentIndex] * strideBytes;
    }
}
