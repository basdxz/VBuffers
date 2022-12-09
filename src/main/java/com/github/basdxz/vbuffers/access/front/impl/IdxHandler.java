package com.github.basdxz.vbuffers.access.front.impl;

import lombok.*;

@AllArgsConstructor
public class IdxHandler {
    protected final int argumentIndex;
    protected final int strideBytes;

    public int strideOffset(Object... args) {
        return (int) args[argumentIndex] * strideBytes;
    }
}
