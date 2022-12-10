package com.github.basdxz.vbuffers.accessor.front.handler.impl;

import lombok.*;

@AllArgsConstructor
public class IdxHandler {
    protected final int argumentIndex;
    protected final int strideBytes;

    public int strideOffset(Object... args) {
        return (int) args[argumentIndex] * strideBytes;
    }
}
