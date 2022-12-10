package com.github.basdxz.vbuffers.accessor.front.handler.impl;

import com.github.basdxz.vbuffers.accessor.front.handler.ReturnHandler;

import java.nio.ByteBuffer;

public class VoidReturnHandler implements ReturnHandler {
    @Override
    public Object handle(Object chainable, ByteBuffer back, int offsetBytes, Object... args) {
        return null;
    }
}
