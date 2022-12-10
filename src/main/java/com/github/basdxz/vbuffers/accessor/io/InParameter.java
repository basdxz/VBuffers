package com.github.basdxz.vbuffers.accessor.io;

import com.github.basdxz.vbuffers.binding.BindingProvider;
import com.github.basdxz.vbuffers.binding.SetterBinding;
import com.github.basdxz.vbuffers.layout.Attribute;
import com.github.basdxz.vbuffers.layout.Layout;
import com.github.basdxz.vbuffers.layout.Stride;
import lombok.*;

import java.nio.ByteBuffer;

public final class InParameter implements Parameter {
    @Getter
    private final int parameterIndex;
    @Getter
    private final Attribute attribute;
    private final SetterBinding<Object> setter;

    @SuppressWarnings("unchecked")
    public InParameter(Stride stride, Layout.In annotation, int parameterIndex) {
        this.parameterIndex = parameterIndex;
        this.attribute = stride.attributes().get(annotation.value());
        this.setter = (SetterBinding<Object>) BindingProvider.setter(this.attribute.type());
    }

    @Override
    public void handle(ByteBuffer backingBuffer, int offsetBytes, Object... args) {
        setter.put(backingBuffer, offsetBytes + attribute.offsetBytes(), args[parameterIndex]);
    }
}
