package com.github.basdxz.vbuffers.access.front.impl;

import com.github.basdxz.vbuffers.access.back.GetterBack;
import com.github.basdxz.vbuffers.access.back.impl.AccessorBacks;
import com.github.basdxz.vbuffers.access.front.AccessFront;
import com.github.basdxz.vbuffers.access.front.ReturnHandler;
import com.github.basdxz.vbuffers.layout.Attribute;
import com.github.basdxz.vbuffers.layout.Stride;
import lombok.*;

import java.nio.ByteBuffer;

public class OutReturnHandler implements ReturnHandler {
    protected final Attribute attribute;
    protected final GetterBack<Object> getterBack;
    protected final int outParameterIndex;

    @SuppressWarnings("unchecked")
    public OutReturnHandler(Stride stride, AccessFront.Out annotation, OutParameterHandler outParameterHandler) {
        val name = annotation.value();
        this.attribute = stride.attributeMap().get(name);
        this.getterBack = (GetterBack<Object>) AccessorBacks.getter(this.attribute.type());
        this.outParameterIndex = outParameterHandler == null ? -1 : outParameterHandler.parameterIndex();
    }

    @Override
    public Object handle(Object chainable, ByteBuffer back, int offsetBytes, Object... args) {
        val outObject = outObject(args);
        return getterBack.get(back, offsetBytes + attribute.offsetBytes(), outObject);
    }

    protected Object outObject(Object... args) {
        if (outParameterIndex == -1)
            return null;
        return args[outParameterIndex];
    }
}
