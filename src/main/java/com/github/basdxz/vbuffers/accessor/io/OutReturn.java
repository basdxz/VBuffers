package com.github.basdxz.vbuffers.accessor.io;

import com.github.basdxz.vbuffers.binding.BindingProvider;
import com.github.basdxz.vbuffers.binding.GetterBinding;
import com.github.basdxz.vbuffers.layout.Attribute;
import com.github.basdxz.vbuffers.layout.Layout;
import com.github.basdxz.vbuffers.layout.Stride;
import lombok.*;

import java.nio.ByteBuffer;

public final class OutReturn implements Return {
    private final Attribute attribute;
    private final GetterBinding<Object> getterBinding;
    private final int outParameterIndex;

    @SuppressWarnings("unchecked")
    public OutReturn(Stride stride, Layout.Out annotation, OutParameter outParameterHandler) {
        val name = annotation.value();
        this.attribute = stride.attributes().get(name);
        this.getterBinding = (GetterBinding<Object>) BindingProvider.getter(this.attribute.type());
        this.outParameterIndex = outParameterHandler == null ? -1 : outParameterHandler.parameterIndex();
    }

    @Override
    public Object handle(Object thiz, ByteBuffer backingBuffer, int offsetBytes, Object... args) {
        val outObject = outObject(args);
        return getterBinding.get(backingBuffer, offsetBytes + attribute.offsetBytes(), outObject);
    }

    private Object outObject(Object... args) {
        if (outParameterIndex == -1)
            return null;
        return args[outParameterIndex];
    }
}
