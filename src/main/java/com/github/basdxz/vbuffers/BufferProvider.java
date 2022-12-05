package com.github.basdxz.vbuffers;

import lombok.*;

import java.lang.reflect.Proxy;

public final class BufferProvider {
    public static <LAYOUT extends VBuffer> LAYOUT newBuffer(@NonNull Class<LAYOUT> layout,
                                                            Allocator allocator) {
        return newBuffer(layout, allocator, 1);
    }

    @SuppressWarnings("unchecked")
    public static <LAYOUT extends VBuffer> LAYOUT newBuffer(@NonNull Class<LAYOUT> layout,
                                                            Allocator allocator,
                                                            int capacity) {
        val classLoader = BufferProvider.class.getClassLoader();
        val interfaces = new Class[]{layout};
        val handler = new VBufferProxyHandler(layout, allocator, capacity);
        return (LAYOUT) Proxy.newProxyInstance(classLoader, interfaces, handler);
    }

}
