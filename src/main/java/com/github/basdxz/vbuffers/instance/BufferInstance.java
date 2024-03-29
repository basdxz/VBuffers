package com.github.basdxz.vbuffers.instance;

import com.github.basdxz.vbuffers.accessor.Accessor;
import com.github.basdxz.vbuffers.accessor.AccessorFactory;
import com.github.basdxz.vbuffers.copy.CopyMask;
import com.github.basdxz.vbuffers.copy.CopyStrategyFactory;
import com.github.basdxz.vbuffers.layout.Layout;
import com.github.basdxz.vbuffers.layout.LayoutInfo;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ReadOnlyBufferException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class BufferInstance<LAYOUT extends Layout<LAYOUT>> implements ExtendedBuffer<LAYOUT>, InvocationHandler {
    private static final ClassLoader CLASS_LOADER = BufferInstance.class.getClassLoader();
    private static final int SPLITERATOR_CHARACTERISTICS = Spliterator.ORDERED |
                                                           Spliterator.DISTINCT |
                                                           Spliterator.SIZED |
                                                           Spliterator.NONNULL |
                                                           Spliterator.IMMUTABLE |
                                                           Spliterator.CONCURRENT |
                                                           Spliterator.SUBSIZED;

    private final Class<LAYOUT> layout;
    private final int capacity;
    private final LAYOUT proxy;
    private final LayoutInfo<LAYOUT> layoutInfo;
    private final ByteBuffer backingBuffer;
    private final Map<Method, Accessor> methodAccessors;

    private int position;
    private int limit;
    private int mark;
    private boolean readOnly;

    // Normal Constructor
    private BufferInstance(Class<LAYOUT> layout, Function<Integer, ByteBuffer> allocator, int capacity) {
        this.layout = layout;
        this.capacity = capacity;
        this.proxy = initProxy();
        this.layoutInfo = new LayoutInfo<>(layout);
        this.backingBuffer = allocator.apply(this.layoutInfo.stride().sizeBytes() * capacity);
        this.methodAccessors = Collections.unmodifiableMap(AccessorFactory.accessFronts(this.layoutInfo));

        // Set Default pointer values
        this.position = 0;
        this.limit = capacity;
        this.mark = -1;
        this.readOnly = false;
    }

    // Copy constructor
    private BufferInstance(BufferInstance<LAYOUT> other) {
        this.layout = other.layout;
        this.capacity = other.capacity;
        this.proxy = initProxy();
        this.layoutInfo = other.layoutInfo;
        this.backingBuffer = other.backingBuffer;
        this.methodAccessors = other.methodAccessors;

        // Copy pointer values
        this.position = other.position;
        this.limit = other.limit;
        this.mark = other.mark;
        this.readOnly = other.readOnly;
    }

    // Slice Copy constructor
    private BufferInstance(BufferInstance<LAYOUT> other, int startIndex, int capacity) {
        this.layout = other.layout;
        this.capacity = capacity;
        this.proxy = initProxy();
        this.layoutInfo = other.layoutInfo;
        this.methodAccessors = other.methodAccessors;

        // Set Default pointer values
        this.position = 0;
        this.limit = capacity;
        this.mark = -1;
        this.readOnly = other.readOnly;

        // Get a slice of the backing buffer from the other handler and set it as the backing buffer
        this.backingBuffer = other.backingBuffer.slice(strideIndexToBytes(startIndex), strideIndexToBytes(capacity));
    }

    @SuppressWarnings("unchecked")
    private LAYOUT initProxy() {
        // Create a new proxy for the handler, must be called after layout is set
        return (LAYOUT) Proxy.newProxyInstance(CLASS_LOADER, new Class[]{layout}, this);
    }

    private BufferInstance<LAYOUT> newCopy() {
        // Create a deep copy of this VBufferHandler
        return new BufferInstance<>(this);
    }

    private BufferInstance<LAYOUT> newCopyOfRemainingStrides() {
        // Create a deep copy of this VBufferHandler, but slice it to only contain the remaining strides
        return new BufferInstance<>(this, position, v$remaining());
    }

    // Static constructor
    public static <LAYOUT extends Layout<LAYOUT>> LAYOUT
    newBuffer(@NonNull Class<LAYOUT> layout, Function<Integer, ByteBuffer> allocator) {
        return newBuffer(layout, allocator, 1);
    }

    // Static constructor
    public static <LAYOUT extends Layout<LAYOUT>> LAYOUT newBuffer(
            @NonNull Class<LAYOUT> layout, Function<Integer, ByteBuffer> allocator, int capacity) {
        return new BufferInstance<>(layout, allocator, capacity).proxy;
    }

    @NotNull
    @Override
    public Iterator<LAYOUT> iterator() {
        return v$iterator();
    }

    @Override
    public LayoutInfo v$layoutInfo() {
        return layoutInfo;
    }

    @Override
    public ByteBuffer v$backing() {
        return backingBuffer;
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
    public LAYOUT v$next() {
        return v$position(position + 1);
    }

    @Override
    public LAYOUT v$position(int newPosition) {
        Objects.checkIndex(newPosition, limit + 1);
        position = newPosition;
        return proxy;
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
        ensureWritable();

        // Copy the remaining strides to the start of the buffer
        val length = v$remaining();
        val source = position;
        v$copyStrides(0, source, length);

        // Reset the pointers
        position = length;
        limit = capacity;
        mark = -1;
        return proxy;
    }

    @Override
    public LAYOUT v$copyStride(int targetIndex, int sourceIndex) {
        ensureWritable();
        return v$copyStrides(targetIndex, sourceIndex, 1);
    }

    @Override
    public LAYOUT v$copyStrides(int targetIndex, int sourceIndex, int length) {
        ensureWritable();
        Objects.checkFromIndexSize(targetIndex, length, limit);
        Objects.checkFromIndexSize(sourceIndex, length, limit);
        if (sourceIndex == targetIndex)
            return proxy;

        // Copy the strides, keeping the positions untouched
        val sourceOffsetBytes = strideIndexToBytes(sourceIndex);
        val targetOffsetBytes = strideIndexToBytes(targetIndex);
        val lengthBytes = strideIndexToBytes(length);
        backingBuffer.put(targetOffsetBytes, backingBuffer, sourceOffsetBytes, lengthBytes);
        return proxy;
    }

    @Override
    public LAYOUT v$put(ExtendedBuffer<?> source) {
        ensureWritable();
        return v$put(source, null);
    }

    @Override
    public LAYOUT v$put(ExtendedBuffer<?> source, @Nullable CopyMask mask) {
        ensureWritable();
        if (!source.v$hasRemaining())
            return proxy;
        val target = this;

        val strideLength = source.v$remaining();
        if (target.v$remaining() < strideLength)
            throw new BufferOverflowException();

        val sourcePosition = source.v$position();
        val targetPosition = target.v$position();

        // Copy the remaining buffer, incrementing the positions
        CopyStrategyFactory.create(source, target, mask).copyRange(sourcePosition, targetPosition, strideLength);
        source.v$position(sourcePosition + strideLength);
        target.v$position(targetPosition + strideLength);
        return proxy;
    }

    private void ensureWritable() {
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
        v$next();
        return singleView;
    }

    @Override
    public LAYOUT v$strideView() {
        return v$strideView(position);
    }

    @Override
    public LAYOUT v$strideView(int index) {
        return new BufferInstance<>(this, index, 1).proxy;
    }

    @Override
    public LAYOUT v$sliceView() {
        return v$sliceView(position, v$remaining());
    }

    @Override
    public LAYOUT v$sliceView(int startIndex, int length) {
        return new BufferInstance<>(this, startIndex, length).proxy;
    }

    @Override
    public LAYOUT v$asReadOnlyView() {
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
        return new BufferIterator<>(newCopyOfRemainingStrides());
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        // Invoke the internal method if it exists, try to handle it like an attribute
        return handleInternalMethod(method, args)
                .orElseGet(() -> handleAttributeMethod(proxy, method, args));
    }

    private Optional<Object> handleInternalMethod(Method method, Object[] args) {
        val methodName = method.getName();

        // Internal methods all start with our prefix, there is also a special case for the iterator method...
        if (!methodName.startsWith(INTERNAL_METHOD_PREFIX) && !methodName.equals("iterator"))
            return Optional.empty();

        // Call the method from this class
        try {
            // Internal method never return null or void
            return Optional.of(method.invoke(this, args));
        } catch (IllegalAccessException | InvocationTargetException e) {
            // If the cause was a runtime exception, report it as the cause instead.
            val cause = e.getCause();
            // If cause is a runtime exception, cast and throw it
            // This causes the stack trace to be the same as the original exception
            // Which is more useful for debugging
            if (cause instanceof RuntimeException)
                throw (RuntimeException) cause;
            throw new RuntimeException("Failed to invoke internal method: %s".formatted(methodName), e);
        }
    }

    private Object handleAttributeMethod(Object proxy, Method method, Object[] args) {
        if (!v$hasRemaining())
            throw new BufferUnderflowException();
        val accessFront = methodAccessors.get(method);
        if (accessFront.writing())
            ensureWritable();
        return accessFront.access(proxy, backingBuffer, strideIndexToBytes(position), args);
    }

    private int attributeOffset(String attributeName) {
        return layoutInfo.stride().attributes().get(attributeName).offsetBytes() + strideIndexToBytes(position);
    }

    private int strideIndexToBytes(int strideIndex) {
        // Convert the stride index to stride bytes
        return strideIndex * layoutInfo.stride().sizeBytes();
    }
}
