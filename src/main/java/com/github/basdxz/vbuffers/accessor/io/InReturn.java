package com.github.basdxz.vbuffers.accessor.io;

import java.nio.ByteBuffer;

public final class InReturn implements Return {
    private final int outParameterIndex;

    public InReturn(InParameter inParameter) {
        this.outParameterIndex = inParameter.parameterIndex();
    }

    @Override
    public Object handle(Object thiz, ByteBuffer backingBuffer, int offsetBytes, Object... args) {
        return args[outParameterIndex];
    }
}
