package com.github.basdxz.vbuffers;

import com.github.basdxz.vbuffers.access.back.GetterBack;
import com.github.basdxz.vbuffers.access.back.SetterBack;
import com.github.basdxz.vbuffers.access.back.impl.AccessorBacks;
import com.github.basdxz.vbuffers.layout.Layout;
import com.github.basdxz.vbuffers.layout.Stride;
import com.github.basdxz.vbuffers.layout.impl.BufferStride;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ReadOnlyBufferException;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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
    protected final Stride stride;
    protected final Map<String, SetterBack<?>> setters;
    protected final Map<String, GetterBack<?>> getters;
    protected final int strideSizeBytes;
    protected final ByteBuffer backing;
    protected final int capacity;

    protected int position;
    protected int limit;
    protected int mark;
    protected boolean readOnly;

    // Normal Constructor
    protected VBufferHandler(Class<LAYOUT> layout, Allocator allocator, int capacity) {
        val layoutAnnotation = layout.getAnnotation(Layout.class);
        Objects.requireNonNull(layoutAnnotation, "Layout interface must have a @Layout annotation");

        val stride = new BufferStride(layout.getAnnotation(Layout.class));
        val strideSizeBytes = stride.sizeBytes();
        val setters = new HashMap<String, SetterBack<?>>();
        val getters = new HashMap<String, GetterBack<?>>();
        for (val attribute : stride.attributeList()) {
            setters.put(attribute.name(), AccessorBacks.setter(attribute.type()));
            getters.put(attribute.name(), AccessorBacks.getter(attribute.type()));
        }

        this.layout = layout;
        this.proxy = initProxy();
        this.stride = stride;
        // Attribute maps are immutable as they are shared between multiple VBufferHandlers
        this.setters = Collections.unmodifiableMap(setters);
        this.getters = Collections.unmodifiableMap(getters);
        this.strideSizeBytes = strideSizeBytes;
        this.backing = allocator.allocate(strideSizeBytes * capacity);
        this.capacity = capacity;
        // Set Default pointer values
        this.position = 0;
        this.limit = capacity;
        this.mark = -1;
        this.readOnly = false;
    }

    // Copy constructor
    protected VBufferHandler(VBufferHandler<LAYOUT> other) {
        this.layout = other.layout;
        this.proxy = initProxy();
        this.stride = other.stride;
        this.setters = other.setters;
        this.getters = other.getters;
        this.strideSizeBytes = other.strideSizeBytes;
        this.backing = other.backing;
        this.capacity = other.capacity;
        this.position = other.position;
        this.limit = other.limit;
        this.mark = other.mark;
        this.readOnly = other.readOnly;
    }

    // Slice Copy constructor
    protected VBufferHandler(VBufferHandler<LAYOUT> other, int startIndex, int size) {
        this.layout = other.layout;
        this.proxy = initProxy();
        this.stride = other.stride;
        this.setters = other.setters;
        this.getters = other.getters;
        this.strideSizeBytes = other.strideSizeBytes;
        this.capacity = size;
        this.position = 0;
        this.limit = size;
        this.mark = -1;
        this.readOnly = other.readOnly;

        // Get a slice of the backing buffer from the other handler and set it as the backing buffer
        this.backing = other.backing.slice(strideIndexToBytes(startIndex), strideIndexToBytes(size));
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
    public ByteBuffer v$backing() {
        return backing;
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
        backing.put(targetOffsetBytes, backing, sourceOffsetBytes, lengthBytes);
        return proxy;
    }

    @Override
    public LAYOUT v$put(LAYOUT source) {
        ensureWritable();
        if (!source.v$hasRemaining())
            return proxy;
        val target = this;

        val strideLength = source.v$remaining();
        if (target.v$remaining() < strideLength)
            throw new BufferOverflowException();
        val lengthBytes = strideIndexToBytes(strideLength);

        val sourceBacking = source.v$backing();
        val sourcePosition = source.v$position();
        val sourceOffsetBytes = strideIndexToBytes(sourcePosition);

        val targetBacking = target.v$backing();
        val targetPosition = target.v$position();
        val targetOffsetBytes = strideIndexToBytes(targetPosition);

        // Copy the remaining buffer, incrementing the positions
        targetBacking.put(targetOffsetBytes, sourceBacking, sourceOffsetBytes, lengthBytes);
        source.v$position(sourcePosition + strideLength);
        target.v$position(targetPosition + strideLength);

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
        v$next();
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
        // Invoke the internal method if it exists, try to handle it like an attribute
        return handleInternalMethod(method, args)
                .orElseGet(() -> handleAttributeMethod(proxy, method, args));
    }

    protected Optional<Object> handleInternalMethod(Method method, Object[] args) {
        val methodName = method.getName();

        // Internal methods all start with our prefix, there is also a special case for the iterator method...
        if (!methodName.startsWith(VBuffer.INTERNAL_METHOD_PREFIX) && !methodName.equals("iterator"))
            return Optional.empty();

        // Call the method from this class
        try {
            // If the method is a VBuffer method, call it
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

    protected Object handleAttributeMethod(Object proxy, Method method, Object[] args) {
        if (!v$hasRemaining())
            throw new BufferUnderflowException();
        val attributeName = method.getName();

        // If the args not null, assume this is a setter with a single argument
        if (args != null) {
            put(attributeName, args[0]);
            return proxy;
        }
        return get(attributeName);
    }

    protected void put(String attributeName, Object value) {
        ensureWritable();
        try {
            attributeSetter(attributeName).put(backing, attributeOffset(attributeName), value);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set attribute %s to value: %s".formatted(attributeName, value.toString()), e);
        }
    }

    protected Object get(String attributeName) {
        try {
            return attributeGetter(attributeName).get(backing, attributeOffset(attributeName), null);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get attribute %s".formatted(attributeName), e);
        }
    }

    @SuppressWarnings("unchecked")
    protected <T> SetterBack<T> attributeSetter(String attributeName) {
        return (SetterBack<T>) setters.get(attributeName);
    }

    @SuppressWarnings("unchecked")
    protected <T> GetterBack<T> attributeGetter(String attributeName) {
        return (GetterBack<T>) getters.get(attributeName);
    }

    protected int attributeOffset(String attributeName) {
        return stride.attributeMap().get(attributeName).offsetBytes() + strideIndexToBytes(position);
    }

    protected int strideIndexToBytes(int strideIndex) {
        // Convert the stride index to stride bytes
        return strideIndex * strideSizeBytes;
    }
}
