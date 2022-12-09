package com.github.basdxz.vbuffers.access.front.impl;

import com.github.basdxz.vbuffers.access.front.ReturnHandler;

import java.nio.ByteBuffer;

public class InReturnHandler implements ReturnHandler {
    protected final int outParameterIndex;

    public InReturnHandler(InParameterHandler inParameter) {
        this.outParameterIndex = inParameter.parameterIndex();
    }

    @Override
    public Object handle(ByteBuffer back, int offsetBytes, Object... args) {
        return args[outParameterIndex];
    }
}
