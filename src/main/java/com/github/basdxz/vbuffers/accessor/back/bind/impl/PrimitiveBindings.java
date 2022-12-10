package com.github.basdxz.vbuffers.accessor.back.bind.impl;

import com.github.basdxz.vbuffers.accessor.back.bind.BackingAccessorBindings;
import lombok.*;

import java.nio.ByteBuffer;

import static com.github.basdxz.vbuffers.helper.BytesConverter.*;

@SuppressWarnings("unused")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PrimitiveBindings implements BackingAccessorBindings {
    @VSetter({byte.class, Byte.class})
    public static void putByte(ByteBuffer buffer, int offsetBytes, Byte value) {
        buffer.put(offsetBytes, value);
    }

    @VImmutableGetter({byte.class, Byte.class})
    public static byte getByte(ByteBuffer buffer, int offsetBytes) {
        return buffer.get(offsetBytes);
    }

    @VSetter({boolean.class, Boolean.class})
    public static void putBoolean(ByteBuffer buffer, int offsetBytes, Boolean value) {
        buffer.put(offsetBytes, (byte) (value ? 1 : 0));
    }

    @VImmutableGetter({boolean.class, Boolean.class})
    public static boolean getBoolean(ByteBuffer buffer, int offsetBytes) {
        return buffer.get(offsetBytes) != 0;
    }

    @VSetter({char.class, Character.class})
    public static void putChar(ByteBuffer buffer, int offsetBytes, Character value) {
        val bytes = new byte[Character.BYTES];
        charToBytes(bytes, 0, value);
        buffer.put(offsetBytes, bytes);
    }

    @VImmutableGetter({char.class, Character.class})
    public static char getChar(ByteBuffer buffer, int offsetBytes) {
        val bytes = new byte[Character.BYTES];
        buffer.get(offsetBytes, bytes);
        return bytesToChar(bytes, 0);
    }

    @VSetter({short.class, Short.class})
    public static void putShort(ByteBuffer buffer, int offsetBytes, Short value) {
        val bytes = new byte[Short.BYTES];
        shortToBytes(bytes, 0, value);
        buffer.put(offsetBytes, bytes);
    }

    @VImmutableGetter({short.class, Short.class})
    public static short getShort(ByteBuffer buffer, int offsetBytes) {
        val bytes = new byte[Short.BYTES];
        buffer.get(offsetBytes, bytes);
        return bytesToShort(bytes, 0);
    }

    @VSetter({int.class, Integer.class})
    public static void putInt(ByteBuffer buffer, int offsetBytes, Integer value) {
        val bytes = new byte[Integer.BYTES];
        intToBytes(bytes, 0, value);
        buffer.put(offsetBytes, bytes);
    }

    @VImmutableGetter({int.class, Integer.class})
    public static int getInt(ByteBuffer buffer, int offsetBytes) {
        val bytes = new byte[Integer.BYTES];
        buffer.get(offsetBytes, bytes);
        return bytesToInt(bytes, 0);
    }

    @VSetter({long.class, Long.class})
    public static void putLong(ByteBuffer buffer, int offsetBytes, Long value) {
        val bytes = new byte[Long.BYTES];
        longToBytes(bytes, 0, value);
        buffer.put(offsetBytes, bytes);
    }

    @VImmutableGetter({long.class, Long.class})
    public static long getLong(ByteBuffer buffer, int offsetBytes) {
        val bytes = new byte[Long.BYTES];
        buffer.get(offsetBytes, bytes);
        return bytesToLong(bytes, 0);
    }

    @VSetter({float.class, Float.class})
    public static void putFloat(ByteBuffer buffer, int offsetBytes, Float value) {
        val bytes = new byte[Float.BYTES];
        floatToBytes(bytes, 0, value);
        buffer.put(offsetBytes, bytes);
    }

    @VImmutableGetter({float.class, Float.class})
    public static float getFloat(ByteBuffer buffer, int offsetBytes) {
        val bytes = new byte[Float.BYTES];
        buffer.get(offsetBytes, bytes);
        return bytesToFloat(bytes, 0);
    }

    @VSetter({double.class, Double.class})
    public static void putDouble(ByteBuffer buffer, int offsetBytes, Double value) {
        val bytes = new byte[Double.BYTES];
        doubleToBytes(bytes, 0, value);
        buffer.put(offsetBytes, bytes);
    }

    @VImmutableGetter({double.class, Double.class})
    public static double getDouble(ByteBuffer buffer, int offsetBytes) {
        val bytes = new byte[Double.BYTES];
        buffer.get(offsetBytes, bytes);
        return bytesToDouble(bytes, 0);
    }
}
