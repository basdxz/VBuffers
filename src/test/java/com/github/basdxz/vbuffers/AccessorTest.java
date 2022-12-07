package com.github.basdxz.vbuffers;

import com.github.basdxz.vbuffers.accessor.AccessorProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

public class AccessorTest {
    protected static final byte TEST_BYTE = 0xA;
    protected static final char TEST_CHAR = 0xA;
    protected static final short TEST_SHORT = 0xAAA;
    protected static final int TEST_INT = 0xAAAAAAAA;
    protected static final long TEST_LONG = 0xAAAAAAAAAAAAAAAAL;
    protected static final float TEST_FLOAT = 0.333_333_253_860_473_632_812F;
    protected static final double TEST_DOUBLE = 0.666_666_664_183_139_801_025_390_625D;
    protected static final boolean TEST_BOOLEAN = true;

    @Test
    public void bytes() {
        // Boxed
        var buffer = ByteBuffer.allocate(Byte.BYTES);

        var setter = AccessorProvider.setter(Byte.class);
        var getter = AccessorProvider.getter(Byte.class);

        setter.put(buffer, 0, TEST_BYTE);
        Assertions.assertEquals(TEST_BYTE, getter.get(buffer, 0, null));

        // Primitive
        buffer = ByteBuffer.allocate(Byte.BYTES);

        setter = AccessorProvider.setter(byte.class);
        getter = AccessorProvider.getter(byte.class);

        setter.put(buffer, 0, TEST_BYTE);
        Assertions.assertEquals(TEST_BYTE, getter.get(buffer, 0, null));
    }

    @Test
    public void chars() {
        // Boxed
        var buffer = ByteBuffer.allocate(Character.BYTES);

        var setter = AccessorProvider.setter(Character.class);
        var getter = AccessorProvider.getter(Character.class);

        setter.put(buffer, 0, TEST_CHAR);
        Assertions.assertEquals(TEST_CHAR, getter.get(buffer, 0, null));

        // Primitive
        buffer = ByteBuffer.allocate(Character.BYTES);

        setter = AccessorProvider.setter(char.class);
        getter = AccessorProvider.getter(char.class);

        setter.put(buffer, 0, TEST_CHAR);
        Assertions.assertEquals(TEST_CHAR, getter.get(buffer, 0, null));
    }

    @Test
    public void shorts() {
        // Boxed
        var buffer = ByteBuffer.allocate(Short.BYTES);

        var setter = AccessorProvider.setter(Short.class);
        var getter = AccessorProvider.getter(Short.class);

        setter.put(buffer, 0, TEST_SHORT);
        Assertions.assertEquals(TEST_SHORT, getter.get(buffer, 0, null));

        // Primitive
        buffer = ByteBuffer.allocate(Short.BYTES);

        setter = AccessorProvider.setter(short.class);
        getter = AccessorProvider.getter(short.class);

        setter.put(buffer, 0, TEST_SHORT);
        Assertions.assertEquals(TEST_SHORT, getter.get(buffer, 0, null));
    }

    @Test
    public void integers() {
        // Boxed
        var buffer = ByteBuffer.allocate(Integer.BYTES);

        var setter = AccessorProvider.setter(Integer.class);
        var getter = AccessorProvider.getter(Integer.class);

        setter.put(buffer, 0, TEST_INT);
        Assertions.assertEquals(TEST_INT, getter.get(buffer, 0, null));

        // Primitive
        buffer = ByteBuffer.allocate(Integer.BYTES);

        setter = AccessorProvider.setter(Integer.class);
        getter = AccessorProvider.getter(Integer.class);

        setter.put(buffer, 0, TEST_INT);
        Assertions.assertEquals(TEST_INT, getter.get(buffer, 0, null));
    }

    @Test
    public void longs() {
        // Boxed
        var buffer = ByteBuffer.allocate(Long.BYTES);

        var setter = AccessorProvider.setter(Long.class);
        var getter = AccessorProvider.getter(Long.class);

        setter.put(buffer, 0, TEST_LONG);
        Assertions.assertEquals(TEST_LONG, getter.get(buffer, 0, null));

        // Primitive
        buffer = ByteBuffer.allocate(Long.BYTES);

        setter = AccessorProvider.setter(long.class);
        getter = AccessorProvider.getter(long.class);

        setter.put(buffer, 0, TEST_LONG);
        Assertions.assertEquals(TEST_LONG, getter.get(buffer, 0, null));
    }

    @Test
    public void floats() {
        // Boxed
        var buffer = ByteBuffer.allocate(Float.BYTES);

        var setter = AccessorProvider.setter(Float.class);
        var getter = AccessorProvider.getter(Float.class);

        setter.put(buffer, 0, TEST_FLOAT);
        Assertions.assertEquals(TEST_FLOAT, getter.get(buffer, 0, null));

        // Primitive
        buffer = ByteBuffer.allocate(Float.BYTES);

        setter = AccessorProvider.setter(float.class);
        getter = AccessorProvider.getter(float.class);

        setter.put(buffer, 0, TEST_FLOAT);
        Assertions.assertEquals(TEST_FLOAT, getter.get(buffer, 0, null));
    }

    @Test
    public void doubles() {
        // Boxed
        var buffer = ByteBuffer.allocate(Double.BYTES);

        var setter = AccessorProvider.setter(Double.class);
        var getter = AccessorProvider.getter(Double.class);

        setter.put(buffer, 0, TEST_DOUBLE);
        Assertions.assertEquals(TEST_DOUBLE, getter.get(buffer, 0, null));

        // Primitive
        buffer = ByteBuffer.allocate(Double.BYTES);

        setter = AccessorProvider.setter(double.class);
        getter = AccessorProvider.getter(double.class);

        setter.put(buffer, 0, TEST_DOUBLE);
        Assertions.assertEquals(TEST_DOUBLE, getter.get(buffer, 0, null));
    }

    @Test
    public void booleans() {
        // Boxed
        var buffer = ByteBuffer.allocate(Byte.BYTES);

        var setter = AccessorProvider.setter(Boolean.class);
        var getter = AccessorProvider.getter(Boolean.class);

        setter.put(buffer, 0, TEST_BOOLEAN);
        Assertions.assertEquals(TEST_BOOLEAN, getter.get(buffer, 0, null));

        // Primitive
        buffer = ByteBuffer.allocate(Byte.BYTES);

        setter = AccessorProvider.setter(boolean.class);
        getter = AccessorProvider.getter(boolean.class);

        setter.put(buffer, 0, TEST_BOOLEAN);
        Assertions.assertEquals(TEST_BOOLEAN, getter.get(buffer, 0, null));
    }
}
