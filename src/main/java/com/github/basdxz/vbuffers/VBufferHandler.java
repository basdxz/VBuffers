package com.github.basdxz.vbuffers;

import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.nio.ByteBuffer;
import java.nio.ReadOnlyBufferException;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static com.github.basdxz.vbuffers.AttributeType.DEFAULT_ATTRIBUTE_TYPES;

//TODO: Rename setters to more put like names
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
    protected final int strideSizeBytes;
    protected final ByteBuffer backing;
    protected final int capacity;

    protected int position;
    protected int limit;
    protected int mark;
    protected boolean readOnly;

    // Normal Constructor
    protected VBufferHandler(Class<LAYOUT> layout, Allocator allocator, int capacity) {
        // Set the layout
        this.layout = layout;

        // Set the default pointers
        this.capacity = capacity;
        this.position = 0;
        this.limit = capacity;
        this.mark = -1;
        this.readOnly = false;

        // Get the layout annotation and ensure it is not null
        val layoutAnnotation = layout.getAnnotation(Layout.class);
        Objects.requireNonNull(layoutAnnotation, "Layout interface must have a @Layout annotation");

        // Creates the attribute offset and type maps
        val attributeOffsets = new HashMap<String, Integer>();
        val attributeTypes = new HashMap<String, AttributeType>();

        // The current offset in bytes, which will be the stride size in bytes once the attributes are processed
        var offsetBytes = 0;

        // Get the attributes from the layout annotation
        for (val attribute : layoutAnnotation.value()) {
            // Get the values from the attribute annotation
            val attributeName = Objects.requireNonNull(attribute.value(), "Attribute name cannot be null");
            val attributeTypeClass = Objects.requireNonNull(attribute.type(), "Attribute type cannot be null");
            val attributeType = Objects.requireNonNull(DEFAULT_ATTRIBUTE_TYPES.get(attributeTypeClass), "Attribute type must be a supported type");

            // Store the attribute offset and type
            attributeOffsets.put(attributeName, offsetBytes);
            attributeTypes.put(attributeName, attributeType);

            // Increment the offset by the attribute size in bytes
            offsetBytes += attributeType.sizeBytes();
        }

        // Set the attribute maps
        this.attributeOffsets = Collections.unmodifiableMap(attributeOffsets);
        this.attributeTypes = Collections.unmodifiableMap(attributeTypes);

        // Set the stride size
        this.strideSizeBytes = offsetBytes;
        // Set the backing buffer
        this.backing = allocator.allocate(offsetBytes * capacity);

        // Create the proxy
        this.proxy = initProxy();
    }

    // Copy constructor
    protected VBufferHandler(VBufferHandler<LAYOUT> other) {
        // Copy values from the other handler
        this.layout = other.layout;
        this.attributeOffsets = other.attributeOffsets;
        this.attributeTypes = other.attributeTypes;
        this.strideSizeBytes = other.strideSizeBytes;
        this.backing = other.backing;
        this.capacity = other.capacity;
        this.position = other.position;
        this.limit = other.limit;
        this.mark = other.mark;
        this.readOnly = other.readOnly;

        // Create a new proxy for the layout
        this.proxy = initProxy();
    }

    // Slice Copy constructor
    protected VBufferHandler(VBufferHandler<LAYOUT> other, int startIndex, int size) {
        // Copy values from the other handler
        this.layout = other.layout;
        this.attributeOffsets = other.attributeOffsets;
        this.attributeTypes = other.attributeTypes;
        this.strideSizeBytes = other.strideSizeBytes;
        this.capacity = size;
        this.position = 0;
        this.limit = size;
        this.mark = -1;
        this.readOnly = other.readOnly;

        // Get a slice of the backing buffer from the other handler and set it as the backing buffer
        this.backing = other.backing.slice(strideIndexToBytes(startIndex), strideIndexToBytes(size));

        // Create a new proxy for the layout
        this.proxy = initProxy();
    }

    @SuppressWarnings("unchecked")
    protected LAYOUT initProxy() {
        // Create a new proxy for the handler, must be called after layout is set
        return (LAYOUT) Proxy.newProxyInstance(CLASS_LOADER, new Class[]{layout}, this);
    }

    protected VBufferHandler<LAYOUT> newCopy() {
        // Create a deep copy of this VBufferHandler
        return new VBufferHandler<>(this);
    }

    protected VBufferHandler<LAYOUT> newCopyOfRemainingStrides() {
        // Create a deep copy of this VBufferHandler, but slice it to only contain the remaining strides
        return new VBufferHandler<>(this, position, v$remaining());
    }

    // Static constructor
    public static <LAYOUT extends VBuffer<LAYOUT>> LAYOUT
    newBuffer(@NonNull Class<LAYOUT> layout, Allocator allocator) {
        return newBuffer(layout, allocator, 1);
    }

    // Static constructor
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
        // Ensure the buffer is writable
        ensureWritable();

        // Copy the remaining strides to the start of the buffer
        val length = v$remaining();
        val source = position;
        v$copyStride(0, source, length);

        // Reset the position and limit
        position = length;
        limit = capacity;
        mark = -1;
        return proxy;
    }

    @Override
    public LAYOUT v$copyStride(int targetIndex, int sourceIndex) {
        // Ensure the buffer is writable
        ensureWritable();
        // Copy a single stride
        return v$copyStride(targetIndex, sourceIndex, 1);
    }

    @Override
    public LAYOUT v$copyStride(int targetIndex, int sourceIndex, int length) {
        // Ensure the buffer is writeable, and that the source and target indices are within bounds
        ensureWritable();
        Objects.checkFromIndexSize(targetIndex, length, limit);
        Objects.checkFromIndexSize(sourceIndex, length, limit);

        // If the source and target indices are the same, do nothing
        if (sourceIndex == targetIndex)
            return proxy;

        // Get the source and target byte offsets and the length in bytes
        val sourceOffsetBytes = strideIndexToBytes(sourceIndex);
        val targetOffsetBytes = strideIndexToBytes(targetIndex);
        val lengthBytes = strideIndexToBytes(length);

        // Copy the stride
        backing.put(targetOffsetBytes, backing, sourceOffsetBytes, lengthBytes);
        return proxy;
    }

    protected void ensureWritable() {
        if (readOnly)
            throw new ReadOnlyBufferException();
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
        return newCopy().proxy;
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
        // Create a copy of this handler and set it to read only
        val copy = newCopy();
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
        return new VBufferIterator<>(newCopyOfRemainingStrides());
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        // Invoke the internal method if it exists, try to invoke a mu
        return handleInternalMethod(proxy, method, args)
                .orElseGet(() -> handleAttributeMethod(proxy, method, args));
    }

    protected Optional<Object> handleInternalMethod(Object proxy, Method method, Object[] args) {
        val methodName = method.getName();
        // Return empty optional if method is not a VBuffer method
        if (!methodName.startsWith(VBuffer.BUFFER_METHOD_PREFIX) && !methodName.equals("iterator"))
            return Optional.empty();
        // Call the method from this class
        try {
            // If the method is a VBuffer method, call it
            // Internal method never return null or void
            return Optional.of(method.invoke(this, args));
        } catch (IllegalAccessException | InvocationTargetException e) {
            val cause = e.getCause();
            // If cause is a read only exception, cast and throw it
            if (cause instanceof ReadOnlyBufferException)
                throw (ReadOnlyBufferException) cause;
            throw new RuntimeException("Failed to invoke internal method: %s".formatted(methodName), e);
        }
    }

    protected Object handleAttributeMethod(Object proxy, Method method, Object[] args) {
        // Get the attribute name
        val attributeName = method.getName();
        // If the method is a setter, set the value and return the proxy
        if (args != null) {
            set(attributeName, args[0]);
            return proxy;
        }
        // Otherwise, return the value
        return get(attributeName);
    }

    protected void set(String attributeName, Object value) {
        ensureWritable();
        try {
            // Set the attribute to provided value
            attributeType(attributeName).set(backing, attributeOffset(attributeName), value);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set attribute %s to value: %s".formatted(attributeName, value.toString()), e);
        }
    }

    protected Object get(String attributeName) {
        try {
            // Get the attribute value
            return attributeType(attributeName).get(backing, attributeOffset(attributeName));
        } catch (Exception e) {
            throw new RuntimeException("Failed to get attribute %s".formatted(attributeName), e);
        }
    }

    protected AttributeType attributeType(String attributeName) {
        // Get the attribute type
        return attributeTypes.get(attributeName);
    }

    protected int attributeOffset(String attributeName) {
        // Get the attribute offset including the position offset
        return attributeOffsets.get(attributeName) + strideIndexToBytes(position);
    }

    protected int strideIndexToBytes(int strideIndex) {
        // Convert the stride index to stride bytes
        return strideIndex * strideSizeBytes;
    }
}
