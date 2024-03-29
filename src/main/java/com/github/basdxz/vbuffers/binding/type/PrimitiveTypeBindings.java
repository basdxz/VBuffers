package com.github.basdxz.vbuffers.binding.type;

import lombok.*;

import java.nio.ByteBuffer;

import static com.github.basdxz.vbuffers.helper.ByteHelper.*;

@SuppressWarnings("unused")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PrimitiveTypeBindings implements TypeBindings {
    @Put({byte.class, Byte.class})
    public static void putByte(ByteBuffer buffer, int offsetBytes, Byte value) {
        buffer.put(offsetBytes, value);
    }

    @NewGet({byte.class, Byte.class})
    public static byte getByte(ByteBuffer buffer, int offsetBytes) {
        return buffer.get(offsetBytes);
    }

    @Put({boolean.class, Boolean.class})
    public static void putBoolean(ByteBuffer buffer, int offsetBytes, Boolean value) {
        buffer.put(offsetBytes, (byte) (value ? 1 : 0));
    }

    @NewGet({boolean.class, Boolean.class})
    public static boolean getBoolean(ByteBuffer buffer, int offsetBytes) {
        return buffer.get(offsetBytes) != 0;
    }

    @Put({char.class, Character.class})
    public static void putChar(ByteBuffer buffer, int offsetBytes, Character value) {
        val bytes = new byte[Character.BYTES];
        charToBytes(bytes, 0, value);
        buffer.put(offsetBytes, bytes);
    }

    @NewGet({char.class, Character.class})
    public static char getChar(ByteBuffer buffer, int offsetBytes) {
        val bytes = new byte[Character.BYTES];
        buffer.get(offsetBytes, bytes);
        return bytesToChar(bytes, 0);
    }

    @Put({short.class, Short.class})
    public static void putShort(ByteBuffer buffer, int offsetBytes, Short value) {
        val bytes = new byte[Short.BYTES];
        shortToBytes(bytes, 0, value);
        buffer.put(offsetBytes, bytes);
    }

    @NewGet({short.class, Short.class})
    public static short getShort(ByteBuffer buffer, int offsetBytes) {
        val bytes = new byte[Short.BYTES];
        buffer.get(offsetBytes, bytes);
        return bytesToShort(bytes, 0);
    }

    @Put({int.class, Integer.class})
    public static void putInt(ByteBuffer buffer, int offsetBytes, Integer value) {
        val bytes = new byte[Integer.BYTES];
        intToBytes(bytes, 0, value);
        buffer.put(offsetBytes, bytes);
    }

    @NewGet({int.class, Integer.class})
    public static int getInt(ByteBuffer buffer, int offsetBytes) {
        val bytes = new byte[Integer.BYTES];
        buffer.get(offsetBytes, bytes);
        return bytesToInt(bytes, 0);
    }

    @Put({long.class, Long.class})
    public static void putLong(ByteBuffer buffer, int offsetBytes, Long value) {
        val bytes = new byte[Long.BYTES];
        longToBytes(bytes, 0, value);
        buffer.put(offsetBytes, bytes);
    }

    @NewGet({long.class, Long.class})
    public static long getLong(ByteBuffer buffer, int offsetBytes) {
        val bytes = new byte[Long.BYTES];
        buffer.get(offsetBytes, bytes);
        return bytesToLong(bytes, 0);
    }

    @Put({float.class, Float.class})
    public static void putFloat(ByteBuffer buffer, int offsetBytes, Float value) {
        val bytes = new byte[Float.BYTES];
        floatToBytes(bytes, 0, value);
        buffer.put(offsetBytes, bytes);
    }

    @NewGet({float.class, Float.class})
    public static float getFloat(ByteBuffer buffer, int offsetBytes) {
        val bytes = new byte[Float.BYTES];
        buffer.get(offsetBytes, bytes);
        return bytesToFloat(bytes, 0);
    }

    @Put({double.class, Double.class})
    public static void putDouble(ByteBuffer buffer, int offsetBytes, Double value) {
        val bytes = new byte[Double.BYTES];
        doubleToBytes(bytes, 0, value);
        buffer.put(offsetBytes, bytes);
    }

    @NewGet({double.class, Double.class})
    public static double getDouble(ByteBuffer buffer, int offsetBytes) {
        val bytes = new byte[Double.BYTES];
        buffer.get(offsetBytes, bytes);
        return bytesToDouble(bytes, 0);
    }
}
