package com.github.basdxz.vbuffers;

import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static com.github.basdxz.vbuffers.AttributeType.DEFAULT_ATTRIBUTE_TYPES;

public class VBufferHandler<LAYOUT extends VBuffer<LAYOUT>> implements VBuffer<LAYOUT>, InvocationHandler {
    protected static final ClassLoader CLASS_LOADER = VBufferHandler.class.getClassLoader();
    protected static final int SPLITERATOR_CHARACTERISTICS = Spliterator.ORDERED |
                                                             Spliterator.DISTINCT |
                                                             Spliterator.SIZED |
                                                             Spliterator.NONNULL |
                                                             Spliterator.IMMUTABLE |
                                                             Spliterator.CONCURRENT |
                                                             Spliterator.SUBSIZED;

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

    // Slice Copy constructor
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

    protected VBufferHandler<LAYOUT> copyRemaining() {
        return new VBufferHandler<>(this, position, v$remaining());
    }

    public static <LAYOUT extends VBuffer<LAYOUT>> LAYOUT
    newBuffer(@NonNull Class<LAYOUT> layout, Allocator allocator) {
        return newBuffer(layout, allocator, 1);
    }

    public static <LAYOUT extends VBuffer<LAYOUT>> LAYOUT newBuffer(
            @NonNull Class<LAYOUT> layout, Allocator allocator, int capacity) {
        return new VBufferHandler<>(layout, allocator, capacity).proxy;
    }

    @NotNull
    @Override
    public Iterator<LAYOUT> iterator() {
        return v$iterator();
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
        if (position < 0 || position > limit)
            throw new IllegalArgumentException("Position out of bounds: " + position);
        this.position = position;
        return proxy;
    }

    @Override
    public LAYOUT v$increment() {
        return v$increment(1);
    }

    @Override
    public LAYOUT v$increment(int indexCount) {
        return v$position(position + indexCount);
    }

    @Override
    public LAYOUT v$decrement() {
        return v$decrement(1);
    }

    @Override
    public LAYOUT v$decrement(int indexCount) {
        return v$position(position - indexCount);
    }

    @Override
    public int v$limit() {
        return limit;
    }

    @Override
    public LAYOUT v$limit(int limit) {
        if (limit < 0 || limit > capacity)
            throw new IllegalArgumentException("Limit out of bounds: " + limit);
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
        val remaining = v$remaining();
        v$copyStride(position, 0, remaining);
        position = remaining;
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
        if (sourceIndex < 0 || sourceIndex + length > limit)
            throw new IllegalArgumentException("Source index out of bounds: " + sourceIndex);
        backing.put(stridesToBytes(targetIndex), backing, stridesToBytes(sourceIndex), stridesToBytes(length));
        return proxy;
    }

    @Override
    @Deprecated
    public LAYOUT v$oldNext() {
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
    public LAYOUT v$duplicateView() {
        return copy().proxy;
    }

    @Override
    public LAYOUT v$nextStrideView() {
        val singleView = v$strideView();
        v$increment();
        return singleView;
    }

    @Override
    public LAYOUT v$strideView() {
        return v$strideView(position);
    }

    @Override
    public LAYOUT v$strideView(int index) {
        return new VBufferHandler<>(this, index, 1).proxy;
    }

    @Override
    public LAYOUT v$sliceView() {
        return v$sliceView(position, v$remaining());
    }

    @Override
    public LAYOUT v$sliceView(int startIndex, int length) {
        return new VBufferHandler<>(this, startIndex, length).proxy;
    }

    @Override
    public LAYOUT v$asReadOnlyView() {
        val copy = copy();
        copy.readOnly = true;
        return copy.proxy;
    }

    @Override
    public Stream<LAYOUT> v$stream() {
        return StreamSupport.stream(v$spliterator(), false);
    }

    @Override
    public Stream<LAYOUT> v$parallelStream() {
        return StreamSupport.stream(v$spliterator(), true);
    }

    @Override
    public Spliterator<LAYOUT> v$spliterator() {
        return Spliterators.spliterator(v$iterator(), v$remaining(), SPLITERATOR_CHARACTERISTICS);
    }

    @Override
    public Iterator<LAYOUT> v$iterator() {
        return new VBufferIterator<>(copyRemaining());
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        return invokeInternal(proxy, method, args)
                .orElseGet(() -> invokeMutator(proxy, method, args));
    }

    protected Optional<Object> invokeInternal(Object proxy, Method method, Object[] args) {
        val methodName = method.getName();
        // Return empty optional if method is not a VBuffer method
        if (!methodName.startsWith(VBuffer.BUFFER_METHOD_PREFIX) && !methodName.equals("iterator"))
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
        val attributeName = method.getName();
        // If the method is a setter, set the value and return the proxy
        if (args != null) {
            if (readOnly)
                throw new UnsupportedOperationException("Buffer is read-only");
            set(attributeName, args[0]);
            return proxy;
        }
        // Otherwise, return the value
        return get(attributeName);
    }

    protected void set(String attributeName, Object value) {
        try {
            attributeType(attributeName).set(backing, attributeOffset(attributeName), value);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set attribute %s to value: %s".formatted(attributeName, value.toString()), e);
        }
    }

    protected Object get(String attributeName) {
        try {
            return attributeType(attributeName).get(backing, attributeOffset(attributeName));
        } catch (Exception e) {
            throw new RuntimeException("Failed to get attribute %s".formatted(attributeName), e);
        }
    }

    protected AttributeType attributeType(String attributeName) {
        return attributeTypes.get(attributeName);
    }

    protected int attributeOffset(String attributeName) {
        return attributeOffsets.get(attributeName) + stridesToBytes(position);
    }

    protected int stridesToBytes(int strideCount) {
        return strideCount * strideBytes;
    }
}
