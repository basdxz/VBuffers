package com.github.basdxz.vbuffers.access.front.impl;

import com.github.basdxz.vbuffers.access.front.ReturnHandler;
import lombok.*;

import java.nio.ByteBuffer;

@AllArgsConstructor
public class ChainReturnHandler implements ReturnHandler {
    @Override
    public Object handle(Object chainable, ByteBuffer back, int offsetBytes, Object... args) {
        return chainable;
    }
}
