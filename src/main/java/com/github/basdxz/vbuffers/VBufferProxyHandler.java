package com.github.basdxz.vbuffers;

import lombok.*;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.*;

import static com.github.basdxz.vbuffers.AttributeType.DEFAULT_ATTRIBUTE_TYPES;

public class VBufferProxyHandler implements VBuffer, InvocationHandler {
    protected final Map<String, Integer> attributeOffsets;
    protected final Map<String, AttributeType> attributeTypes;
    protected final int strideBytes;
    protected final ByteBuffer buffer;
    protected final int capacity;
    protected int position;
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
    public int v$capacity() {
        return capacity;
    }

    @Override
    public int v$position() {
        return position;
    }

    @Override
    public void v$position(int position) {
        this.position = position;
    }

    @Override
    public int v$limit() {
        return limit;
    }

    @Override
    public void v$limit(int limit) {
        this.limit = limit;
    }

    @Override
    public void v$mark() {
        mark = position;
    }

    @Override
    public void v$reset() {
        position = mark;
    }

    @Override
    public void v$clear() {
        limit = capacity;
        position = 0;
        mark = -1;
    }

    @Override
    public void v$flip() {
        limit = position;
        position = 0;
        mark = -1;
    }

    @Override
    public void v$rewind() {
        position = 0;
        mark = -1;
    }

    @Override
    public void v$compact() {
        val size = limit - position;
        v$copy(position, 0, size);
        position = size;
        limit = capacity;
        mark = -1;
    }

    @Override
    public void v$copy(int sourceIndex, int targetIndex) {
        v$copy(sourceIndex, targetIndex, 1);
    }

    @Override
    public void v$copy(int sourceIndex, int targetIndex, int length) {
        val sourceOffsetBytes = sourceIndex * strideBytes;
        val targetOffsetBytes = targetIndex * strideBytes;
        val lengthBytes = length * strideBytes;
        buffer.put(targetOffsetBytes, buffer, sourceOffsetBytes, lengthBytes);
    }

    @Override
    public boolean v$next() {
        if (position >= limit)
            return false;
        position++;
        return true;
    }

    @Override
    public boolean v$hasRemaining() {
        return position < limit;
    }

    @Override
    public int v$remaining() {
        return limit - position;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        return invokeInternal(proxy, method, args)
                .orElseGet(() -> invokeMutator(proxy, method, args));
    }

    protected Optional<Object> invokeInternal(Object proxy, Method method, Object[] args) {
        val methodName = method.getName();
        // Return empty optional if method is not a VBuffer method
        if (!methodName.startsWith(VBuffer.BUFFER_METHOD_PREFIX))
            return Optional.empty();
        // If args is null, set it to an empty array
        if (args == null)
            args = new Object[0];
        // Call the method from this class
        try {
            var result = method.invoke(this, args);
            if (result == null)
                result = 0;
            return Optional.of(result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected Object invokeMutator(Object proxy, Method method, Object[] args) {
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
