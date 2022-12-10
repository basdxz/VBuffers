package com.github.basdxz.vbuffers.accessor.front.bind.impl;

import com.github.basdxz.vbuffers.accessor.back.BackingGetter;
import com.github.basdxz.vbuffers.accessor.back.impl.BackingAccessors;
import com.github.basdxz.vbuffers.accessor.front.bind.ReturnBinding;
import com.github.basdxz.vbuffers.layout.Attribute;
import com.github.basdxz.vbuffers.layout.Layout;
import com.github.basdxz.vbuffers.layout.Stride;
import lombok.*;

import java.nio.ByteBuffer;

public class OutReturnBinding implements ReturnBinding {
    protected final Attribute attribute;
    protected final BackingGetter<Object> backingGetter;
    protected final int outParameterIndex;

    @SuppressWarnings("unchecked")
    public OutReturnBinding(Stride stride, Layout.Out annotation, OutParameterBinding outParameterHandler) {
        val name = annotation.value();
        this.attribute = stride.attributes().get(name);
        this.backingGetter = (BackingGetter<Object>) BackingAccessors.getter(this.attribute.type());
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
