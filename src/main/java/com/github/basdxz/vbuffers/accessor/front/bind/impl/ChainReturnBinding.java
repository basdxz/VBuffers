package com.github.basdxz.vbuffers.accessor.front.bind.impl;

import com.github.basdxz.vbuffers.accessor.front.bind.ReturnBinding;
import lombok.*;

import java.nio.ByteBuffer;

@AllArgsConstructor
public class ChainReturnBinding implements ReturnBinding {
    @Override
    public Object handle(Object chainable, ByteBuffer back, int offsetBytes, Object... args) {
        return chainable;
    }
}
