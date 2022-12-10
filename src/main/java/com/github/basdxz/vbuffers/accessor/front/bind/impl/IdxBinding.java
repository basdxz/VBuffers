package com.github.basdxz.vbuffers.accessor.front.bind.impl;

import lombok.*;

@AllArgsConstructor
public class IdxBinding {
    protected final int argumentIndex;
    protected final int strideBytes;

    public int strideOffset(Object... args) {
        return (int) args[argumentIndex] * strideBytes;
    }
}
