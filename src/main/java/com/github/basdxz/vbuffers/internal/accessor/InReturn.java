package com.github.basdxz.vbuffers.internal.accessor;

import java.nio.ByteBuffer;

final class InReturn implements AccessorReturn {
    private final int outParameterIndex;

    InReturn(InParameter inParameter) {
        this.outParameterIndex = inParameter.parameterIndex();
    }

    @Override
    public Object handle(Object chainable, ByteBuffer back, int offsetBytes, Object... args) {
        return args[outParameterIndex];
    }
}