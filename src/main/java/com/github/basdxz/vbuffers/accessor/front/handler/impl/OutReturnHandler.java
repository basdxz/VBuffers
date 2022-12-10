package com.github.basdxz.vbuffers.accessor.front.handler.impl;

import com.github.basdxz.vbuffers.accessor.back.BackingGetter;
import com.github.basdxz.vbuffers.accessor.back.impl.BackingAccessorFactory;
import com.github.basdxz.vbuffers.accessor.front.handler.ReturnHandler;
import com.github.basdxz.vbuffers.layout.Attribute;
import com.github.basdxz.vbuffers.layout.Layout;
import com.github.basdxz.vbuffers.layout.Stride;
import lombok.*;

import java.nio.ByteBuffer;

public class OutReturnHandler implements ReturnHandler {
    protected final Attribute attribute;
    protected final BackingGetter<Object> backingGetter;
    protected final int outParameterIndex;

    @SuppressWarnings("unchecked")
    public OutReturnHandler(Stride stride, Layout.Out annotation, OutParameterHandler outParameterHandler) {
        val name = annotation.value();
        this.attribute = stride.attributeMap().get(name);
        this.backingGetter = (BackingGetter<Object>) BackingAccessorFactory.getter(this.attribute.type());
        this.outParameterIndex = outParameterHandler == null ? -1 : outParameterHandler.parameterIndex();
    }

    @Override
    public Object handle(Object chainable, ByteBuffer back, int offsetBytes, Object... args) {
        val outObject = outObject(args);
        return backingGetter.get(back, offsetBytes + attribute.offsetBytes(), outObject);
    }

    protected Object outObject(Object... args) {
        if (outParameterIndex == -1)
            return null;
        return args[outParameterIndex];
    }
}
