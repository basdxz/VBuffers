package com.github.basdxz.vbuffers.internal.accessor;

import com.github.basdxz.vbuffers.layout.Attribute;

import java.nio.ByteBuffer;

interface AccessorParameter {
    Attribute attribute();

    int parameterIndex();

    void handle(ByteBuffer back, int offsetBytes, Object... args);
}
