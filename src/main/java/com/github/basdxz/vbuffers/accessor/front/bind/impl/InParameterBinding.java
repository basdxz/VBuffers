package com.github.basdxz.vbuffers.accessor.front.bind.impl;

import com.github.basdxz.vbuffers.accessor.back.BackingSetter;
import com.github.basdxz.vbuffers.accessor.back.impl.BackingAccessors;
import com.github.basdxz.vbuffers.accessor.front.bind.ParameterBinding;
import com.github.basdxz.vbuffers.layout.Attribute;
import com.github.basdxz.vbuffers.layout.Layout;
import com.github.basdxz.vbuffers.layout.Stride;
import lombok.*;

import java.nio.ByteBuffer;

public class InParameterBinding implements ParameterBinding {
    @Getter
    protected final int parameterIndex;
    @Getter
    protected final Attribute attribute;
    protected final BackingSetter<Object> setter;

    @SuppressWarnings("unchecked")
    public InParameterBinding(Stride stride, Layout.In annotation, int parameterIndex) {
        this.parameterIndex = parameterIndex;
        this.attribute = stride.attributes().get(annotation.value());
        this.setter = (BackingSetter<Object>) BackingAccessors.setter(this.attribute.type());
    }

    @Override
    public void handle(ByteBuffer back, int offsetBytes, Object... args) {
        setter.put(back, offsetBytes + attribute.offsetBytes(), args[parameterIndex]);
    }
}
