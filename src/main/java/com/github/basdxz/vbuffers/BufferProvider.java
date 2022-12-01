package com.github.basdxz.vbuffers;

import lombok.*;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public final class BufferProvider {
    @SuppressWarnings("unchecked")
    public static <LAYOUT extends VBuffer> LAYOUT newBuffer(@NonNull Class<LAYOUT> layout, ByteBuffer backing) {
        val classLoader = BufferProvider.class.getClassLoader();
        val interfaces = new Class[]{layout};
        val invocationHandler = new BufferInvocationHandler(layout, backing);
        return (LAYOUT) Proxy.newProxyInstance(classLoader, interfaces, invocationHandler);
    }

    public static class BufferInvocationHandler implements InvocationHandler {
        protected final Map<String, Integer> attributeOffsets = new HashMap<>();
        protected final Map<String, Class<?>> attributeTypes = new HashMap<>();
        protected final ByteBuffer backing;

        public BufferInvocationHandler(Class<?> layout, ByteBuffer backing) {
            this.backing = backing;

            val layoutAnnotation = layout.getAnnotation(Layout.class);
            var offset = 0;
            for (Layout.Attribute attribute : layoutAnnotation.value()) {
                attributeOffsets.put(attribute.value(), offset);
                attributeTypes.put(attribute.value(), attribute.type());
                offset += 4;
            }
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) {
            val key = method.getName();
            if (args != null) {
                write(key, (int) args[0]);
                return proxy;
            }
            return read(key);
        }

        protected void write(String key, int value) {
            backing.putInt(keyOffset(key), value);
        }

        protected int read(String key) {
            return backing.getInt(keyOffset(key));
        }

        protected int keyOffset(String key) {
            return attributeOffsets.get(key);
        }
    }
}
