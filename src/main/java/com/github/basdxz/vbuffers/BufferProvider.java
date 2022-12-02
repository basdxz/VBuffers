package com.github.basdxz.vbuffers;

import lombok.*;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import static com.github.basdxz.vbuffers.AttributeType.DEFAULT_ATTRIBUTE_TYPES;
import static java.util.Objects.requireNonNull;

public final class BufferProvider {
    public static <LAYOUT extends VBuffer> LAYOUT newBuffer(@NonNull Class<LAYOUT> layout,
                                                            BackingProvider backingProvider) {
        return newBuffer(layout, backingProvider, 1);
    }

    @SuppressWarnings("unchecked")
    public static <LAYOUT extends VBuffer> LAYOUT newBuffer(@NonNull Class<LAYOUT> layout,
                                                            BackingProvider backingProvider,
                                                            int sizeStrides) {
        val classLoader = BufferProvider.class.getClassLoader();
        val interfaces = new Class[]{layout};
        val handler = new BufferHandler(layout, backingProvider, sizeStrides);
        return (LAYOUT) Proxy.newProxyInstance(classLoader, interfaces, handler);
    }

    public static class BufferHandler implements InvocationHandler {
        protected final Map<String, Integer> attributeOffsets = new HashMap<>();
        protected final Map<String, AttributeType> attributeTypes = new HashMap<>();
        protected final int sizeStrides;
        protected final ByteBuffer backing;

        public BufferHandler(Class<?> layout, BackingProvider backingProvider, int sizeStrides) {
            this.sizeStrides = sizeStrides;
            val layoutAnnotation = layout.getAnnotation(Layout.class);
            var offset = 0;
            for (Layout.Attribute attribute : layoutAnnotation.value()) {
                val name = requireNonNull(attribute.value());
                this.attributeOffsets.put(attribute.value(), offset);
                val typeClass = requireNonNull(attribute.type());
                val type = requireNonNull(DEFAULT_ATTRIBUTE_TYPES.get(attribute.type()));

                offset += type.sizeBytes();
                this.attributeTypes.put(attribute.value(), type);
            }
            this.backing = backingProvider.newBacking(offset * sizeStrides);
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) {
            val key = method.getName();
            if (args != null) {
                set(key, args[0]);
                return proxy;
            }
            return get(key);
        }

        protected void set(String key, Object value) {
            attributeType(key).set(backing, keyOffset(key), value);
        }

        protected Object get(String key) {
            return attributeType(key).get(backing, keyOffset(key));
        }

        protected AttributeType attributeType(String key) {
            return attributeTypes.get(key);
        }

        protected int keyOffset(String key) {
            return attributeOffsets.get(key);
        }
    }
}
