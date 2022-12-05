package com.github.basdxz.vbuffers;

import lombok.*;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.nio.ByteBuffer;
import java.util.*;

import static com.github.basdxz.vbuffers.AttributeType.DEFAULT_ATTRIBUTE_TYPES;

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
        val handler = new BufferHandler(layout, allocator, capacity);
        return (LAYOUT) Proxy.newProxyInstance(classLoader, interfaces, handler);
    }

    public static class BufferHandler implements InvocationHandler {
        protected final Map<String, Integer> attributeOffsets;
        protected final Map<String, AttributeType> attributeTypes;
        protected final int strideBytes;
        protected final ByteBuffer buffer;
        protected final int capacity;
        protected int position;
        protected int limit;
        protected int mark;

        public BufferHandler(Class<?> layout, Allocator allocator, int capacity) {
            val layoutAnnotation = layout.getAnnotation(Layout.class);
            var attributeOffsets = new HashMap<String, Integer>();
            var attributeTypes = new HashMap<String, AttributeType>();
            var offset = 0;
            for (Layout.Attribute attribute : layoutAnnotation.value()) {
                val name = Objects.requireNonNull(attribute.value());
                attributeOffsets.put(attribute.value(), offset);
                val typeClass = Objects.requireNonNull(attribute.type());
                val type = Objects.requireNonNull(DEFAULT_ATTRIBUTE_TYPES.get(attribute.type()));

                offset += type.sizeBytes();
                attributeTypes.put(attribute.value(), type);
            }
            this.attributeOffsets = Collections.unmodifiableMap(attributeOffsets);
            this.attributeTypes = Collections.unmodifiableMap(attributeTypes);
            this.strideBytes = offset;
            this.buffer = allocator.allocate(offset * capacity);
            this.capacity = capacity;
            this.position = 0;
            this.limit = capacity;
            this.mark = -1;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) {
            return handleInternal(proxy, method, args)
                    .orElseGet(() -> handleAttribute(proxy, method, args));
        }

        protected Optional<Object> handleInternal(Object proxy, Method method, Object[] args) {
            try {
                val arg = args != null && args.length > 0 ? args[0] : null;
                switch (method.getName()) {
                    case "capacity" -> {
                        return Optional.of(capacity());
                    }
                    case "position" -> {
                        if (arg == null)
                            return Optional.of(position());
                        position((Integer) arg);
                        return Optional.of(0);
                    }
                    case "limit" -> {
                        if (arg == null)
                            return Optional.of(limit());
                        limit((Integer) arg);
                        return Optional.of(0);
                    }
                    case "mark" -> {
                        mark();
                        return Optional.of(0);
                    }
                    case "reset" -> {
                        reset();
                        return Optional.of(0);
                    }
                    case "clear" -> {
                        clear();
                        return Optional.of(0);
                    }
                    case "flip" -> {
                        flip();
                        return Optional.of(0);
                    }
                    case "rewind" -> {
                        rewind();
                        return Optional.of(0);
                    }
                    case "compact" -> {
                        compact();
                        return Optional.of(0);
                    }
                    case "next" -> {
                        return Optional.of(next());
                    }
                    case "hasRemaining" -> {
                        return Optional.of(hasRemaining());
                    }
                    case "remaining" -> {
                        return Optional.of(remaining());
                    }
                    default -> {
                        return Optional.empty();
                    }
                }
            } catch (Throwable e) {
                return Optional.empty();
            }
        }

        protected int capacity() {
            return capacity;
        }

        protected int position() {
            return position;
        }

        protected void position(int position) {
            this.position = position;
        }

        protected int limit() {
            return limit;
        }

        protected void limit(int limit) {
            this.limit = limit;
        }

        protected void mark() {
            mark = position;
        }

        protected void reset() {
            position = mark;
        }

        protected void clear() {
            limit = capacity;
            position = 0;
            mark = -1;
        }

        protected void flip() {
            limit = position;
            position = 0;
            mark = -1;
        }

        protected void rewind() {
            position = 0;
            mark = -1;
        }

        protected void compact() {
            val size = limit - position;
            move(position, 0, size);
            position = size;
            limit = capacity;
            mark = -1;
        }

        protected void move(int sourceIndex, int targetIndex, int length) {
            for (var i = 0; i < length; i++)
                move(sourceIndex + i, targetIndex + i);
        }

        protected void move(int sourceIndex, int targetIndex) {
            val sourceOffsetBytes = sourceIndex * strideBytes;
            val targetOffsetBytes = targetIndex * strideBytes;
            for (var i = 0; i < strideBytes; i++)
                buffer.put(targetOffsetBytes + i, buffer.get(sourceOffsetBytes + i));
        }

        protected boolean next() {
            if (position < limit) {
                position++;
                return true;
            }
            return false;
        }

        protected boolean hasRemaining() {
            return position < limit;
        }

        protected int remaining() {
            return limit - position;
        }

        protected Object handleAttribute(Object proxy, Method method, Object[] args) {
            val key = method.getName();
            try {
                if (args != null) {
                    set(key, args[0]);
                    return proxy;
                }
                return get(key);
            } catch (Exception e) {
                throw new RuntimeException("Failed to handle key " + key, e);
            }
        }

        protected void set(String key, Object value) {
            attributeType(key).set(buffer, keyOffset(key), value);
        }

        protected Object get(String key) {
            return attributeType(key).get(buffer, keyOffset(key));
        }

        protected AttributeType attributeType(String key) {
            return attributeTypes.get(key);
        }

        protected int keyOffset(String key) {
            return attributeOffsets.get(key) + (strideBytes * position);
        }
    }
}
