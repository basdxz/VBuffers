package com.github.basdxz.vbuffers.access.front.impl;

import com.github.basdxz.vbuffers.access.front.ReturnHandler;
import lombok.*;

import java.nio.ByteBuffer;

@AllArgsConstructor
public class ChainReturnHandler implements ReturnHandler {
    protected final Object chainable;

    @Override
    public Object handle(ByteBuffer back, int offsetBytes, Object... args) {
        return chainable;
    }
}
