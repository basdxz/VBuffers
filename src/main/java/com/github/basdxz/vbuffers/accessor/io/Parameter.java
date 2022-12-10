package com.github.basdxz.vbuffers.accessor.io;

import com.github.basdxz.vbuffers.layout.Attribute;

import java.nio.ByteBuffer;

public sealed interface Parameter permits InParameter, OutParameter {
    Attribute attribute();

    int parameterIndex();

    void handle(ByteBuffer back, int offsetBytes, Object... args);
}
