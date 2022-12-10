package com.github.basdxz.vbuffers.accessor.front.bind.impl;

import com.github.basdxz.vbuffers.accessor.front.bind.ReturnBinding;

import java.nio.ByteBuffer;

public class InReturnBinding implements ReturnBinding {
    protected final int outParameterIndex;

    public InReturnBinding(InParameterBinding inParameter) {
        this.outParameterIndex = inParameter.parameterIndex();
    }

    @Override
    public Object handle(Object chainable, ByteBuffer back, int offsetBytes, Object... args) {
        return args[outParameterIndex];
    }
}
