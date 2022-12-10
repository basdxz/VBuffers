package com.github.basdxz.vbuffers.accessor.front.bind.impl;

import com.github.basdxz.vbuffers.accessor.back.BackingGetter;
import com.github.basdxz.vbuffers.accessor.back.impl.BackingAccessors;
import com.github.basdxz.vbuffers.accessor.front.bind.ParameterBinding;
import com.github.basdxz.vbuffers.layout.Attribute;
import com.github.basdxz.vbuffers.layout.Layout;
import com.github.basdxz.vbuffers.layout.Stride;
import lombok.*;

import java.nio.ByteBuffer;

@AllArgsConstructor
public class OutParameterBinding implements ParameterBinding {
    @Getter
    protected final int parameterIndex;
    @Getter
    protected final Attribute attribute;
    protected final BackingGetter<Object> getter;

    @SuppressWarnings("unchecked")
    public OutParameterBinding(Stride stride, Layout.Out annotation, int parameterIndex) {
        this.parameterIndex = parameterIndex;
        this.attribute = stride.attributes().get(annotation.value());
        this.getter = (BackingGetter<Object>) BackingAccessors.setter(this.attribute.type());
        if (getter instanceof BackingGetter.Immutable)
            throw new IllegalArgumentException("Cannot use immutable getter for out parameter");
    }

    @Override
    public void handle(ByteBuffer back, int offsetBytes, Object... args) {
        getter.get(back, offsetBytes + attribute.offsetBytes(), args[parameterIndex]);
    }
}