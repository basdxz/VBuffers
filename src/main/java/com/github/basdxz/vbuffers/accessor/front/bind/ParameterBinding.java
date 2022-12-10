package com.github.basdxz.vbuffers.accessor.front.bind;

import com.github.basdxz.vbuffers.layout.Attribute;

import java.nio.ByteBuffer;

public interface ParameterBinding {
    Attribute attribute();

    int parameterIndex();

    void handle(ByteBuffer back, int offsetBytes, Object... args);
}
