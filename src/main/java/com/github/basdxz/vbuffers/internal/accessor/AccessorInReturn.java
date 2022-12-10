package com.github.basdxz.vbuffers.internal.accessor;

import java.nio.ByteBuffer;

final class AccessorInReturn implements AccessorReturn {
    private final int outParameterIndex;

    AccessorInReturn(AccessorInParameter inParameter) {
        this.outParameterIndex = inParameter.parameterIndex();
    }

    @Override
    public Object handle(Object chainable, ByteBuffer back, int offsetBytes, Object... args) {
        return args[outParameterIndex];
    }
}
