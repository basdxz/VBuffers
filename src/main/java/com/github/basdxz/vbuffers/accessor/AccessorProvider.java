package com.github.basdxz.vbuffers.accessor;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class AccessorProvider {
    protected static final Map<Class<?>, Setter<?>> setters = new HashMap<>();
    protected static final Map<Class<?>, Getter<?>> getters = new HashMap<>();

    static {
        setters.put(Byte.class, (Setter<Byte>) ByteBuffer::put);
        setters.put(Character.class, (Setter<Character>) (buffer1, index, value1) -> buffer1.putShort(index, (short) value1.charValue()));
        setters.put(Short.class, (Setter<Short>) ByteBuffer::putShort);
        setters.put(Integer.class, (Setter<Integer>) ByteBuffer::putInt);
        setters.put(Long.class, (Setter<Long>) ByteBuffer::putLong);
        setters.put(Float.class, (Setter<Float>) ByteBuffer::putFloat);
        setters.put(Double.class, (Setter<Double>) ByteBuffer::putDouble);
        setters.put(Boolean.class, (Setter<Boolean>) (buffer, offset, value) -> buffer.put(offset, (byte) (value ? 1 : 0)));

        getters.put(Byte.class, (Getter.Immutable<Byte>) ByteBuffer::get);
        getters.put(Character.class, (Getter.Immutable<Character>) (buffer1, index) -> (char) buffer1.getShort(index));
        getters.put(Short.class, (Getter.Immutable<Short>) ByteBuffer::getShort);
        getters.put(Integer.class, (Getter.Immutable<Integer>) ByteBuffer::getInt);
        getters.put(Long.class, (Getter.Immutable<Long>) ByteBuffer::getLong);
        getters.put(Float.class, (Getter.Immutable<Float>) ByteBuffer::getFloat);
        getters.put(Double.class, (Getter.Immutable<Double>) ByteBuffer::getDouble);
        getters.put(Boolean.class, (Getter.Immutable<Boolean>) (buffer, offset) -> buffer.get(offset) != 0);
    }

    public static <T> Setter<T> setter(Class<T> type) {
        return (Setter<T>) setters.get(toBoxed(type));
    }

    public static <T> Getter<T> getter(Class<T> type) {
        return (Getter<T>) getters.get(toBoxed(type));
    }

    private static Class<?> toBoxed(Class<?> type) {
        if (type == byte.class)
            return Byte.class;
        if (type == char.class)
            return Character.class;
        if (type == short.class)
            return Short.class;
        if (type == int.class)
            return Integer.class;
        if (type == long.class)
            return Long.class;
        if (type == float.class)
            return Float.class;
        if (type == double.class)
            return Double.class;
        if (type == boolean.class)
            return Boolean.class;
        return type;
    }
}