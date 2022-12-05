package com.github.basdxz.vbuffers;

import lombok.*;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.nio.ByteBuffer;
import java.util.*;

import static com.github.basdxz.vbuffers.AttributeType.DEFAULT_ATTRIBUTE_TYPES;

public class VBufferHandler<LAYOUT extends VBuffer<LAYOUT>> implements VBuffer<LAYOUT>, InvocationHandler {
    protected static final ClassLoader CLASS_LOADER = VBufferHandler.class.getClassLoader();

    protected final Class<LAYOUT> layout;
    protected final LAYOUT proxy;
    protected final Map<String, Integer> attributeOffsets;
    protected final Map<String, AttributeType> attributeTypes;
    protected final int strideBytes;
    protected final ByteBuffer backing;
    protected final int capacity;
    protected int position;
    protected int limit;
    protected int mark;
    protected boolean readOnly;

    public VBufferHandler(Class<LAYOUT> layout, Allocator allocator, int capacity) {
        val layoutAnnotation = layout.getAnnotation(Layout.class);
        val attributeOffsets = new HashMap<String, Integer>();
        val attributeTypes = new HashMap<String, AttributeType>();
        var offset = 0;
        for (val attribute : layoutAnnotation.value()) {
            val name = Objects.requireNonNull(attribute.value());
            attributeOffsets.put(attribute.value(), offset);
            val typeClass = Objects.requireNonNull(attribute.type());
            val type = Objects.requireNonNull(DEFAULT_ATTRIBUTE_TYPES.get(attribute.type()));

            offset += type.sizeBytes();
            attributeTypes.put(attribute.value(), type);
        }
        this.layout = layout;
        this.proxy = initProxy();
        this.attributeOffsets = Collections.unmodifiableMap(attributeOffsets);
        this.attributeTypes = Collections.unmodifiableMap(attributeTypes);
        this.strideBytes = offset;
        this.backing = allocator.allocate(offset * capacity);
        this.capacity = capacity;
        this.position = 0;
        this.limit = capacity;
        this.mark = -1;
        this.readOnly = false;
    }

    // Copy constructor
    public VBufferHandler(VBufferHandler<LAYOUT> other) {
        this.layout = other.layout;
        this.proxy = initProxy();
        this.attributeOffsets = other.attributeOffsets;
        this.attributeTypes = other.attributeTypes;
        this.strideBytes = other.strideBytes;
        this.backing = other.backing;
        this.capacity = other.capacity;
        this.position = other.position;
        this.limit = other.limit;
        this.mark = other.mark;
        this.readOnly = other.readOnly;
    }

    // Slice constructor
    public VBufferHandler(VBufferHandler<LAYOUT> other, int startIndex, int size) {
        this.layout = other.layout;
        this.proxy = initProxy();
        this.attributeOffsets = other.attributeOffsets;
        this.attributeTypes = other.attributeTypes;
        this.strideBytes = other.strideBytes;
        this.backing = other.backing.slice(stridesToBytes(startIndex), stridesToBytes(size));
        this.capacity = size;
        this.position = 0;
        this.limit = size;
        this.mark = -1;
        this.readOnly = other.readOnly;
    }

    @SuppressWarnings("unchecked")
    protected LAYOUT initProxy() {
        return (LAYOUT) Proxy.newProxyInstance(CLASS_LOADER, new Class[]{layout}, this);
    }

    protected VBufferHandler<LAYOUT> copy() {
        return new VBufferHandler<>(this);
    }

    public static <LAYOUT extends VBuffer<LAYOUT>> LAYOUT newBuffer(@NonNull Class<LAYOUT> layout,
                                                                    Allocator allocator) {
        return newBuffer(layout, allocator, 1);
    }

    public static <LAYOUT extends VBuffer<LAYOUT>> LAYOUT newBuffer(@NonNull Class<LAYOUT> layout,
                                                                    Allocator allocator,
                                                                    int capacity) {
        return new VBufferHandler<>(layout, allocator, capacity).proxy;
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
    public LAYOUT v$position(int position) {
        this.position = position;
        return proxy;
    }

    @Override
    public int v$limit() {
        return limit;
    }

    @Override
    public LAYOUT v$limit(int limit) {
        this.limit = limit;
        return proxy;
    }

    @Override
    public LAYOUT v$mark() {
        mark = position;
        return proxy;
    }

    @Override
    public LAYOUT v$reset() {
        position = mark;
        return proxy;
    }

    @Override
    public LAYOUT v$clear() {
        limit = capacity;
        position = 0;
        mark = -1;
        return proxy;
    }

    @Override
    public LAYOUT v$flip() {
        limit = position;
        position = 0;
        mark = -1;
        return proxy;
    }

    @Override
    public LAYOUT v$rewind() {
        position = 0;
        mark = -1;
        return proxy;
    }

    @Override
    public LAYOUT v$compact() {
        val size = limit - position;
        v$copyStride(position, 0, size);
        position = size;
        limit = capacity;
        mark = -1;
        return proxy;
    }

    @Override
    public LAYOUT v$copyStride(int sourceIndex, int targetIndex) {
        v$copyStride(sourceIndex, targetIndex, 1);
        return proxy;
    }

    @Override
    public LAYOUT v$copyStride(int sourceIndex, int targetIndex, int length) {
        backing.put(stridesToBytes(targetIndex), backing, stridesToBytes(sourceIndex), stridesToBytes(length));
        return proxy;
    }

    @Override
    public LAYOUT v$next() {
        if (position < limit)
            position++;
        return proxy;
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
    public LAYOUT v$duplicate() {
        return copy().proxy;
    }

    @Override
    public LAYOUT v$single() {
        return v$single(position);
    }

    @Override
    public LAYOUT v$single(int index) {
        return new VBufferHandler<>(this, index, 1).proxy;
    }

    @Override
    public LAYOUT v$slice() {
        return v$slice(position, limit - position);
    }

    @Override
    public LAYOUT v$slice(int startIndex, int length) {
        return new VBufferHandler<>(this, startIndex, length).proxy;
    }

    @Override
    public LAYOUT v$asReadOnly() {
        val copy = copy();
        copy.readOnly = true;
        return copy.proxy;
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
        // Get the attribute name
        val key = method.getName();
        try {
            // If the method is a setter, set the value and return the proxy
            if (args != null) {
                if (readOnly)
                    throw new UnsupportedOperationException("Buffer is read-only");
                set(key, args[0]);
                return proxy;
            }
            // Otherwise, return the value
            return get(key);
        } catch (Exception e) {
            throw new RuntimeException("Failed to handle key " + key, e);
        }
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
        return attributeOffsets.get(key) + stridesToBytes(position);
    }

    protected int stridesToBytes(int index) {
        return index * strideBytes;
    }
}
