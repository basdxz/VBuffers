package com.github.basdxz.vbuffers.internal.accessor;

import com.github.basdxz.vbuffers.binding.SetterBinding;
import com.github.basdxz.vbuffers.internal.binding.BindingProvider;
import com.github.basdxz.vbuffers.layout.Attribute;
import com.github.basdxz.vbuffers.layout.Layout;
import com.github.basdxz.vbuffers.layout.Stride;
import lombok.*;

import java.nio.ByteBuffer;

final class InParameter implements AccessorParameter {
    @Getter
    private final int parameterIndex;
    @Getter
    private final Attribute attribute;
    private final SetterBinding<Object> setter;

    @SuppressWarnings("unchecked")
    InParameter(Stride stride, Layout.In annotation, int parameterIndex) {
        this.parameterIndex = parameterIndex;
        this.attribute = stride.attributes().get(annotation.value());
        this.setter = (SetterBinding<Object>) BindingProvider.setter(this.attribute.type());
    }

    @Override
    public void handle(ByteBuffer back, int offsetBytes, Object... args) {
        setter.put(back, offsetBytes + attribute.offsetBytes(), args[parameterIndex]);
    }
}
