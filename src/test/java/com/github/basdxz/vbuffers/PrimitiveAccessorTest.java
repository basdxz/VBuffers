package com.github.basdxz.vbuffers;

import com.github.basdxz.vbuffers.accessor.backing.impl.AccessorBackingProvider;
import lombok.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

public class PrimitiveAccessorTest {
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
        val setter = AccessorBackingProvider.setter(byte.class);
        val getter = AccessorBackingProvider.getter(byte.class);

        setter.put(buffer, OFFSET_BYTES, TEST_BYTE);
        val result = getter.get(buffer, OFFSET_BYTES, null);

        Assertions.assertEquals(TEST_BYTE, result);
    }

    @Test
    public void boxedByte() {
        val buffer = ByteBuffer.allocate(BUFFER_SIZE_BYTES);
        val setter = AccessorBackingProvider.setter(Byte.class);
        val getter = AccessorBackingProvider.getter(Byte.class);

        setter.put(buffer, OFFSET_BYTES, TEST_BYTE);
        val result = getter.get(buffer, OFFSET_BYTES, null);

        Assertions.assertEquals(TEST_BYTE, result);
    }

    @Test
    public void primitiveBoolean() {
        val buffer = ByteBuffer.allocate(BUFFER_SIZE_BYTES);
        val setter = AccessorBackingProvider.setter(boolean.class);
        val getter = AccessorBackingProvider.getter(boolean.class);

        setter.put(buffer, OFFSET_BYTES, TEST_BOOLEAN);
        val result = getter.get(buffer, OFFSET_BYTES, null);

        Assertions.assertEquals(TEST_BOOLEAN, result);
    }

    @Test
    public void boxedBoolean() {
        val buffer = ByteBuffer.allocate(BUFFER_SIZE_BYTES);
        val setter = AccessorBackingProvider.setter(Boolean.class);
        val getter = AccessorBackingProvider.getter(Boolean.class);

        setter.put(buffer, OFFSET_BYTES, TEST_BOOLEAN);
        val result = getter.get(buffer, OFFSET_BYTES, null);

        Assertions.assertEquals(TEST_BOOLEAN, result);
    }

    @Test
    public void primitiveChar() {
        val buffer = ByteBuffer.allocate(BUFFER_SIZE_BYTES);
        val setter = AccessorBackingProvider.setter(char.class);
        val getter = AccessorBackingProvider.getter(char.class);

        setter.put(buffer, OFFSET_BYTES, TEST_CHAR);
        val result = getter.get(buffer, OFFSET_BYTES, null);

        Assertions.assertEquals(TEST_CHAR, result);
    }

    @Test
    public void boxedChar() {
        val buffer = ByteBuffer.allocate(BUFFER_SIZE_BYTES);
        val setter = AccessorBackingProvider.setter(Character.class);
        val getter = AccessorBackingProvider.getter(Character.class);

        setter.put(buffer, OFFSET_BYTES, TEST_CHAR);
        val result = getter.get(buffer, OFFSET_BYTES, null);

        Assertions.assertEquals(TEST_CHAR, result);
    }

    @Test
    public void primitiveShort() {
        val buffer = ByteBuffer.allocate(BUFFER_SIZE_BYTES);
        val setter = AccessorBackingProvider.setter(short.class);
        val getter = AccessorBackingProvider.getter(short.class);

        setter.put(buffer, OFFSET_BYTES, TEST_SHORT);
        val result = getter.get(buffer, OFFSET_BYTES, null);

        Assertions.assertEquals(TEST_SHORT, result);
    }

    @Test
    public void boxedShort() {
        val buffer = ByteBuffer.allocate(BUFFER_SIZE_BYTES);
        val setter = AccessorBackingProvider.setter(Short.class);
        val getter = AccessorBackingProvider.getter(Short.class);

        setter.put(buffer, OFFSET_BYTES, TEST_SHORT);
        val result = getter.get(buffer, OFFSET_BYTES, null);

        Assertions.assertEquals(TEST_SHORT, result);
    }

    @Test
    public void primitiveInt() {
        val buffer = ByteBuffer.allocate(BUFFER_SIZE_BYTES);
        val setter = AccessorBackingProvider.setter(int.class);
        val getter = AccessorBackingProvider.getter(int.class);

        setter.put(buffer, OFFSET_BYTES, TEST_INT);
        val result = getter.get(buffer, OFFSET_BYTES, null);

        Assertions.assertEquals(TEST_INT, result);
    }

    @Test
    public void boxedInt() {
        val buffer = ByteBuffer.allocate(BUFFER_SIZE_BYTES);
        val setter = AccessorBackingProvider.setter(Integer.class);
        val getter = AccessorBackingProvider.getter(Integer.class);

        setter.put(buffer, OFFSET_BYTES, TEST_INT);
        val result = getter.get(buffer, OFFSET_BYTES, null);

        Assertions.assertEquals(TEST_INT, result);
    }

    @Test
    public void primitiveLong() {
        val buffer = ByteBuffer.allocate(BUFFER_SIZE_BYTES);
        val setter = AccessorBackingProvider.setter(long.class);
        val getter = AccessorBackingProvider.getter(long.class);

        setter.put(buffer, OFFSET_BYTES, TEST_LONG);
        val result = getter.get(buffer, OFFSET_BYTES, null);

        Assertions.assertEquals(TEST_LONG, result);
    }

    @Test
    public void boxedLong() {
        val buffer = ByteBuffer.allocate(BUFFER_SIZE_BYTES);
        val setter = AccessorBackingProvider.setter(Long.class);
        val getter = AccessorBackingProvider.getter(Long.class);

        setter.put(buffer, OFFSET_BYTES, TEST_LONG);
        val result = getter.get(buffer, OFFSET_BYTES, null);

        Assertions.assertEquals(TEST_LONG, result);
    }

    @Test
    public void primitiveFloat() {
        val buffer = ByteBuffer.allocate(BUFFER_SIZE_BYTES);
        val setter = AccessorBackingProvider.setter(float.class);
        val getter = AccessorBackingProvider.getter(float.class);

        setter.put(buffer, OFFSET_BYTES, TEST_FLOAT);
        val result = getter.get(buffer, OFFSET_BYTES, null);

        Assertions.assertEquals(TEST_FLOAT, result);
    }

    @Test
    public void boxedFloat() {
        val buffer = ByteBuffer.allocate(BUFFER_SIZE_BYTES);
        val setter = AccessorBackingProvider.setter(Float.class);
        val getter = AccessorBackingProvider.getter(Float.class);

        setter.put(buffer, OFFSET_BYTES, TEST_FLOAT);
        val result = getter.get(buffer, OFFSET_BYTES, null);

        Assertions.assertEquals(TEST_FLOAT, result);
    }

    @Test
    public void primitiveDouble() {
        val buffer = ByteBuffer.allocate(BUFFER_SIZE_BYTES);
        val setter = AccessorBackingProvider.setter(double.class);
        val getter = AccessorBackingProvider.getter(double.class);

        setter.put(buffer, OFFSET_BYTES, TEST_DOUBLE);
        val result = getter.get(buffer, OFFSET_BYTES, null);

        Assertions.assertEquals(TEST_DOUBLE, result);
    }

    @Test
    public void boxedDouble() {
        val buffer = ByteBuffer.allocate(BUFFER_SIZE_BYTES);
        val setter = AccessorBackingProvider.setter(Double.class);
        val getter = AccessorBackingProvider.getter(Double.class);

        setter.put(buffer, OFFSET_BYTES, TEST_DOUBLE);
        val result = getter.get(buffer, OFFSET_BYTES, null);

        Assertions.assertEquals(TEST_DOUBLE, result);
    }
}
