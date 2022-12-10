package com.github.basdxz.vbuffers.accessor.front.handler.impl;

import com.github.basdxz.vbuffers.accessor.back.BackingGetter;
import com.github.basdxz.vbuffers.accessor.back.impl.BackingAccessorFactory;
import com.github.basdxz.vbuffers.accessor.front.handler.ParameterHandler;
import com.github.basdxz.vbuffers.layout.Attribute;
import com.github.basdxz.vbuffers.layout.Layout;
import com.github.basdxz.vbuffers.layout.Stride;
import lombok.*;

import java.nio.ByteBuffer;

@AllArgsConstructor
public class OutParameterHandler implements ParameterHandler {
    @Getter
    protected final int parameterIndex;
    @Getter
    protected final Attribute attribute;
    protected final BackingGetter<Object> getter;

    @SuppressWarnings("unchecked")
    public OutParameterHandler(Stride stride, Layout.Out annotation, int parameterIndex) {
        this.parameterIndex = parameterIndex;
        this.attribute = stride.attributeMap().get(annotation.value());
        this.getter = (BackingGetter<Object>) BackingAccessorFactory.setter(this.attribute.type());
        if (getter instanceof BackingGetter.Immutable)
            throw new IllegalArgumentException("Cannot use immutable getter for out parameter");
    }

    @Override
    public void handle(ByteBuffer back, int offsetBytes, Object... args) {
        getter.get(back, offsetBytes + attribute.offsetBytes(), args[parameterIndex]);
    }
}
