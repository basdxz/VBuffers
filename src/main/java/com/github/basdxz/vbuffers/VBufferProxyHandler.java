package com.github.basdxz.vbuffers;

import lombok.*;
import lombok.experimental.*;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.*;

import static com.github.basdxz.vbuffers.AttributeType.DEFAULT_ATTRIBUTE_TYPES;

@Accessors(chain = false)
public class VBufferProxyHandler implements VBuffer, InvocationHandler {
    protected final Map<String, Integer> attributeOffsets;
    protected final Map<String, AttributeType> attributeTypes;
    protected final int strideBytes;
    protected final ByteBuffer buffer;
    @Getter
    protected final int capacity;
    @Setter
    @Getter
    protected int position;
    @Setter
    @Getter
    protected int limit;
    protected int mark;

    public VBufferProxyHandler(Class<?> layout, Allocator allocator, int capacity) {
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
    public void mark() {
        mark = position;
    }

    @Override
    public void reset() {
        position = mark;
    }

    @Override
    public void clear() {
        limit = capacity;
        position = 0;
        mark = -1;
    }

    @Override
    public void flip() {
        limit = position;
        position = 0;
        mark = -1;
    }

    @Override
    public void rewind() {
        position = 0;
        mark = -1;
    }

    @Override
    public void compact() {
        val size = limit - position;
        copy(position, 0, size);
        position = size;
        limit = capacity;
        mark = -1;
    }

    @Override
    public void copy(int sourceIndex, int targetIndex) {
        copy(sourceIndex, targetIndex, 1);
    }

    @Override
    public void copy(int sourceIndex, int targetIndex, int length) {
        val sourceOffsetBytes = sourceIndex * strideBytes;
        val targetOffsetBytes = targetIndex * strideBytes;
        val lengthBytes = length * strideBytes;
        buffer.put(targetOffsetBytes, buffer, sourceOffsetBytes, lengthBytes);
    }

    @Override
    public boolean next() {
        if (position >= limit)
            return false;
        position++;
        return true;
    }

    @Override
    public boolean hasRemaining() {
        return position < limit;
    }

    @Override
    public int remaining() {
        return limit - position;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        return handleInternal(proxy, method, args)
                .orElseGet(() -> handleAttribute(proxy, method, args));
    }

    protected Optional<Object> handleInternal(Object proxy, Method method, Object[] args) {
        try {
            val firstArg = args != null && args.length > 0 ? args[0] : null;
            switch (method.getName()) {
                case "capacity" -> {
                    return Optional.of(capacity());
                }
                case "position" -> {
                    if (firstArg == null)
                        return Optional.of(position());
                    position((Integer) firstArg);
                    return Optional.of(0);
                }
                case "limit" -> {
                    if (firstArg == null)
                        return Optional.of(limit());
                    limit((Integer) firstArg);
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
                case "copy" -> {
                    if (firstArg == null)
                        return Optional.empty();
                    if (args.length == 2) {
                        copy((Integer) args[0], (Integer) args[1]);
                    } else {
                        copy((Integer) args[0], (Integer) args[1], (Integer) args[2]);
                    }
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
