package com.github.basdxz.vbuffers.access.front.impl;

import com.github.basdxz.vbuffers.access.back.GetterBack;
import com.github.basdxz.vbuffers.access.back.impl.AccessorBacks;
import com.github.basdxz.vbuffers.access.front.AccessFront;
import com.github.basdxz.vbuffers.access.front.ParameterHandler;
import com.github.basdxz.vbuffers.layout.Attribute;
import com.github.basdxz.vbuffers.layout.Stride;
import lombok.*;

import java.nio.ByteBuffer;

@AllArgsConstructor
public class OutParameterHandler implements ParameterHandler {
    @Getter
    protected final int parameterIndex;
    @Getter
    protected final Attribute attribute;
    protected final GetterBack<Object> getter;

    @SuppressWarnings("unchecked")
    public OutParameterHandler(Stride stride, AccessFront.Out annotation, int parameterIndex) {
        this.parameterIndex = parameterIndex;
        this.attribute = stride.attributeMap().get(annotation.value());
        this.getter = (GetterBack<Object>) AccessorBacks.setter(this.attribute.type());
        if (getter instanceof GetterBack.Immutable)
            throw new IllegalArgumentException("Cannot use immutable getter for out parameter");
    }

    @Override
    public void handle(ByteBuffer back, int offsetBytes, Object... args) {
        getter.get(back, offsetBytes + attribute.offsetBytes(), args[parameterIndex]);
    }
}
