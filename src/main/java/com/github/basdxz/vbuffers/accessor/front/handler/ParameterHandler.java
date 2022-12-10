package com.github.basdxz.vbuffers.accessor.front.handler;

import com.github.basdxz.vbuffers.layout.Attribute;

import java.nio.ByteBuffer;

public interface ParameterHandler {
    Attribute attribute();

    int parameterIndex();

    void handle(ByteBuffer back, int offsetBytes, Object... args);
}
