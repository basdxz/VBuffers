package com.github.basdxz.vbuffers.accessor.front.handler.impl;

import com.github.basdxz.vbuffers.accessor.back.BackingSetter;
import com.github.basdxz.vbuffers.accessor.back.impl.BackingAccessorFactory;
import com.github.basdxz.vbuffers.accessor.front.handler.ParameterHandler;
import com.github.basdxz.vbuffers.layout.Attribute;
import com.github.basdxz.vbuffers.layout.Layout;
import com.github.basdxz.vbuffers.layout.Stride;
import lombok.*;

import java.nio.ByteBuffer;

public class InParameterHandler implements ParameterHandler {
    @Getter
    protected final int parameterIndex;
    @Getter
    protected final Attribute attribute;
    protected final BackingSetter<Object> setter;

    @SuppressWarnings("unchecked")
    public InParameterHandler(Stride stride, Layout.In annotation, int parameterIndex) {
        this.parameterIndex = parameterIndex;
        this.attribute = stride.attributeMap().get(annotation.value());
        this.setter = (BackingSetter<Object>) BackingAccessorFactory.setter(this.attribute.type());
    }

    @Override
    public void handle(ByteBuffer back, int offsetBytes, Object... args) {
        setter.put(back, offsetBytes + attribute.offsetBytes(), args[parameterIndex]);
    }
}
