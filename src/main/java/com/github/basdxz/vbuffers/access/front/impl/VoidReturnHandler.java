package com.github.basdxz.vbuffers.access.front.impl;

import com.github.basdxz.vbuffers.access.front.ReturnHandler;

import java.nio.ByteBuffer;

public class VoidReturnHandler implements ReturnHandler {
    @Override
    public Object handle(ByteBuffer back, int offsetBytes, Object... args) {
        return null;
    }
}
