package com.github.basdxz.vbuffers.accessor.front.bind.impl;

import com.github.basdxz.vbuffers.accessor.front.bind.ReturnBinding;

import java.nio.ByteBuffer;

public class VoidReturnBinding implements ReturnBinding {
    @Override
    public Object handle(Object chainable, ByteBuffer back, int offsetBytes, Object... args) {
        return null;
    }
}
