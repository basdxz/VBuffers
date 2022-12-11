package com.github.basdxz.vbuffers.layout;

import lombok.*;

import java.lang.reflect.Method;
import java.util.List;

@Getter
public class LayoutInfo {
    protected final Class<? extends Layout<?>> type;
    protected final Stride stride;
    protected final List<Method> methods;

    public LayoutInfo(Class<? extends Layout<?>> type) {
        this.type = type;
        this.stride = new Stride(type.getAnnotation(Layout.Stride.class));
        this.methods = List.of(type.getDeclaredMethods());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof LayoutInfo other))
            return false;
        return type.equals(other.type);
    }
}
