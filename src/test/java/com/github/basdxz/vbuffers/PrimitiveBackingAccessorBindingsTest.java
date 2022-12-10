package com.github.basdxz.vbuffers;

import com.github.basdxz.vbuffers.accessor.back.impl.BackingAccessors;
import lombok.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

public class PrimitiveBackingAccessorBindingsTest {
    protected static final int BUFFER_SIZE_BYTES = 1024;
    protected static final int OFFSET_BYTES = 456;

    protected static final byte TEST_BYTE = 0xA;
    protected static final char TEST_CHAR = 0xA;
    protected static final short TEST_SHORT = 0xAAA;
    protected static final int TEST_INT = 0xAAAAAAAA;
    protected static final long TEST_LONG = 0xAAAAAAAAAAAAAAAAL;
    protected static final float TEST_FLOAT = 0.333_333_253_860_473_632_812F;
    protected static final double TEST_DOUBLE = 0.666_666_664_183_139_801_025_390_625D;
    protected static final boolean TEST_BOOLEAN = true;

    @Test
    public void primitiveByte() {
        val buffer = ByteBuffer.allocate(BUFFER_SIZE_BYTES);
        val setter = BackingAccessors.setter(byte.class);
        val getter = BackingAccessors.getter(byte.class);

        setter.put(buffer, OFFSET_BYTES, TEST_BYTE);
        val result = getter.get(buffer, OFFSET_BYTES, null);

        Assertions.assertEquals(TEST_BYTE, result);
    }

    @Test
    public void boxedByte() {
        val buffer = ByteBuffer.allocate(BUFFER_SIZE_BYTES);
        val setter = BackingAccessors.setter(Byte.class);
        val getter = BackingAccessors.getter(Byte.class);

        setter.put(buffer, OFFSET_BYTES, TEST_BYTE);
        val result = getter.get(buffer, OFFSET_BYTES, null);

        Assertions.assertEquals(TEST_BYTE, result);
    }

    @Test
    public void primitiveBoolean() {
        val buffer = ByteBuffer.allocate(BUFFER_SIZE_BYTES);
        val setter = BackingAccessors.setter(boolean.class);
        val getter = BackingAccessors.getter(boolean.class);

        setter.put(buffer, OFFSET_BYTES, TEST_BOOLEAN);
        val result = getter.get(buffer, OFFSET_BYTES, null);

        Assertions.assertEquals(TEST_BOOLEAN, result);
    }

    @Test
    public void boxedBoolean() {
        val buffer = ByteBuffer.allocate(BUFFER_SIZE_BYTES);
        val setter = BackingAccessors.setter(Boolean.class);
        val getter = BackingAccessors.getter(Boolean.class);

        setter.put(buffer, OFFSET_BYTES, TEST_BOOLEAN);
        val result = getter.get(buffer, OFFSET_BYTES, null);

        Assertions.assertEquals(TEST_BOOLEAN, result);
    }

    @Test
    public void primitiveChar() {
        val buffer = ByteBuffer.allocate(BUFFER_SIZE_BYTES);
        val setter = BackingAccessors.setter(char.class);
        val getter = BackingAccessors.getter(char.class);

        setter.put(buffer, OFFSET_BYTES, TEST_CHAR);
        val result = getter.get(buffer, OFFSET_BYTES, null);

        Assertions.assertEquals(TEST_CHAR, result);
    }

    @Test
    public void boxedChar() {
        val buffer = ByteBuffer.allocate(BUFFER_SIZE_BYTES);
        val setter = BackingAccessors.setter(Character.class);
        val getter = BackingAccessors.getter(Character.class);

        setter.put(buffer, OFFSET_BYTES, TEST_CHAR);
        val result = getter.get(buffer, OFFSET_BYTES, null);

        Assertions.assertEquals(TEST_CHAR, result);
    }

    @Test
    public void primitiveShort() {
        val buffer = ByteBuffer.allocate(BUFFER_SIZE_BYTES);
        val setter = BackingAccessors.setter(short.class);
        val getter = BackingAccessors.getter(short.class);

        setter.put(buffer, OFFSET_BYTES, TEST_SHORT);
        val result = getter.get(buffer, OFFSET_BYTES, null);

        Assertions.assertEquals(TEST_SHORT, result);
    }

    @Test
    public void boxedShort() {
        val buffer = ByteBuffer.allocate(BUFFER_SIZE_BYTES);
        val setter = BackingAccessors.setter(Short.class);
        val getter = BackingAccessors.getter(Short.class);

        setter.put(buffer, OFFSET_BYTES, TEST_SHORT);
        val result = getter.get(buffer, OFFSET_BYTES, null);

        Assertions.assertEquals(TEST_SHORT, result);
    }

    @Test
    public void primitiveInt() {
        val buffer = ByteBuffer.allocate(BUFFER_SIZE_BYTES);
        val setter = BackingAccessors.setter(int.class);
        val getter = BackingAccessors.getter(int.class);

        setter.put(buffer, OFFSET_BYTES, TEST_INT);
        val result = getter.get(buffer, OFFSET_BYTES, null);

        Assertions.assertEquals(TEST_INT, result);
    }

    @Test
    public void boxedInt() {
        val buffer = ByteBuffer.allocate(BUFFER_SIZE_BYTES);
        val setter = BackingAccessors.setter(Integer.class);
        val getter = BackingAccessors.getter(Integer.class);

        setter.put(buffer, OFFSET_BYTES, TEST_INT);
        val result = getter.get(buffer, OFFSET_BYTES, null);

        Assertions.assertEquals(TEST_INT, result);
    }

    @Test
    public void primitiveLong() {
        val buffer = ByteBuffer.allocate(BUFFER_SIZE_BYTES);
        val setter = BackingAccessors.setter(long.class);
        val getter = BackingAccessors.getter(long.class);

        setter.put(buffer, OFFSET_BYTES, TEST_LONG);
        val result = getter.get(buffer, OFFSET_BYTES, null);

        Assertions.assertEquals(TEST_LONG, result);
    }

    @Test
    public void boxedLong() {
        val buffer = ByteBuffer.allocate(BUFFER_SIZE_BYTES);
        val setter = BackingAccessors.setter(Long.class);
        val getter = BackingAccessors.getter(Long.class);

        setter.put(buffer, OFFSET_BYTES, TEST_LONG);
        val result = getter.get(buffer, OFFSET_BYTES, null);

        Assertions.assertEquals(TEST_LONG, result);
    }

    @Test
    public void primitiveFloat() {
        val buffer = ByteBuffer.allocate(BUFFER_SIZE_BYTES);
        val setter = BackingAccessors.setter(float.class);
        val getter = BackingAccessors.getter(float.class);

        setter.put(buffer, OFFSET_BYTES, TEST_FLOAT);
        val result = getter.get(buffer, OFFSET_BYTES, null);

        Assertions.assertEquals(TEST_FLOAT, result);
    }

    @Test
    public void boxedFloat() {
        val buffer = ByteBuffer.allocate(BUFFER_SIZE_BYTES);
        val setter = BackingAccessors.setter(Float.class);
        val getter = BackingAccessors.getter(Float.class);

        setter.put(buffer, OFFSET_BYTES, TEST_FLOAT);
        val result = getter.get(buffer, OFFSET_BYTES, null);

        Assertions.assertEquals(TEST_FLOAT, result);
    }

    @Test
    public void primitiveDouble() {
        val buffer = ByteBuffer.allocate(BUFFER_SIZE_BYTES);
        val setter = BackingAccessors.setter(double.class);
        val getter = BackingAccessors.getter(double.class);

        setter.put(buffer, OFFSET_BYTES, TEST_DOUBLE);
        val result = getter.get(buffer, OFFSET_BYTES, null);

        Assertions.assertEquals(TEST_DOUBLE, result);
    }

    @Test
    public void boxedDouble() {
        val buffer = ByteBuffer.allocate(BUFFER_SIZE_BYTES);
        val setter = BackingAccessors.setter(Double.class);
        val getter = BackingAccessors.getter(Double.class);

        setter.put(buffer, OFFSET_BYTES, TEST_DOUBLE);
        val result = getter.get(buffer, OFFSET_BYTES, null);

        Assertions.assertEquals(TEST_DOUBLE, result);
    }
}
