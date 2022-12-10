package com.github.basdxz.vbuffers.internal.accessor;

import com.github.basdxz.vbuffers.binding.GetterBinding;
import com.github.basdxz.vbuffers.internal.binding.BindingProvider;
import com.github.basdxz.vbuffers.layout.Attribute;
import com.github.basdxz.vbuffers.layout.Layout;
import com.github.basdxz.vbuffers.layout.Stride;
import lombok.*;

import java.nio.ByteBuffer;

@AllArgsConstructor
final class OutParameter implements AccessorParameter {
    @Getter
    private final int parameterIndex;
    @Getter
    private final Attribute attribute;
    private final GetterBinding<Object> getter;

    @SuppressWarnings("unchecked")
    OutParameter(Stride stride, Layout.Out annotation, int parameterIndex) {
        this.parameterIndex = parameterIndex;
        this.attribute = stride.attributes().get(annotation.value());
        this.getter = (GetterBinding<Object>) BindingProvider.setter(this.attribute.type());
        if (getter instanceof GetterBinding.Immutable)
            throw new IllegalArgumentException("Cannot use immutable getter for out parameter");
    }

    @Override
    public void handle(ByteBuffer back, int offsetBytes, Object... args) {
        getter.get(back, offsetBytes + attribute.offsetBytes(), args[parameterIndex]);
    }
}
