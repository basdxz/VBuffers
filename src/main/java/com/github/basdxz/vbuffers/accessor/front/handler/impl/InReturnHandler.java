package com.github.basdxz.vbuffers.accessor.front.handler.impl;

import com.github.basdxz.vbuffers.accessor.front.handler.ReturnHandler;

import java.nio.ByteBuffer;

public class InReturnHandler implements ReturnHandler {
    protected final int outParameterIndex;

    public InReturnHandler(InParameterHandler inParameter) {
        this.outParameterIndex = inParameter.parameterIndex();
    }

    @Override
    public Object handle(Object chainable, ByteBuffer back, int offsetBytes, Object... args) {
        return args[outParameterIndex];
    }
}
