package com.github.basdxz.vbuffers.accessor.io;

import com.github.basdxz.vbuffers.binding.BindingProvider;
import com.github.basdxz.vbuffers.binding.GetterBinding;
import com.github.basdxz.vbuffers.layout.Attribute;
import com.github.basdxz.vbuffers.layout.Layout;
import com.github.basdxz.vbuffers.layout.Stride;
import lombok.*;

import java.nio.ByteBuffer;

public final class OutParameter implements Parameter {
    @Getter
    private final int parameterIndex;
    @Getter
    private final Attribute attribute;
    private final GetterBinding<Object> getter;

    @SuppressWarnings("unchecked")
    public OutParameter(Stride stride, Layout.Out annotation, int parameterIndex) {
        this.parameterIndex = parameterIndex;
        this.attribute = stride.attributes().get(annotation.value());
        this.getter = (GetterBinding<Object>) BindingProvider.setter(this.attribute.type());
        if (getter instanceof GetterBinding.Allocating)
            throw new IllegalArgumentException("Cannot use immutable getter for out parameter");
    }

    @Override
    public void handle(ByteBuffer backingBuffer, int offsetBytes, Object... args) {
        getter.get(backingBuffer, offsetBytes + attribute.offsetBytes(), args[parameterIndex]);
    }
}
