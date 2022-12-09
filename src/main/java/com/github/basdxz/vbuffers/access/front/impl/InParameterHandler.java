package com.github.basdxz.vbuffers.access.front.impl;

import com.github.basdxz.vbuffers.access.back.SetterBack;
import com.github.basdxz.vbuffers.access.back.impl.AccessorBacks;
import com.github.basdxz.vbuffers.access.front.AccessFront;
import com.github.basdxz.vbuffers.access.front.ParameterHandler;
import com.github.basdxz.vbuffers.layout.Attribute;
import com.github.basdxz.vbuffers.layout.Stride;
import lombok.*;

import java.nio.ByteBuffer;

public class InParameterHandler implements ParameterHandler {
    @Getter
    protected final int parameterIndex;
    @Getter
    protected final Attribute attribute;
    protected final SetterBack<Object> setter;

    @SuppressWarnings("unchecked")
    public InParameterHandler(Stride stride, AccessFront.In annotation, int parameterIndex) {
        this.parameterIndex = parameterIndex;
        this.attribute = stride.attributeMap().get(annotation.value());
        this.setter = (SetterBack<Object>) AccessorBacks.setter(this.attribute.type());
    }

    @Override
    public void handle(ByteBuffer back, int offsetBytes, Object... args) {
        setter.put(back, offsetBytes + attribute.offsetBytes(), args[parameterIndex]);
    }
}
