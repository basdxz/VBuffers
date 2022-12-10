package com.github.basdxz.vbuffers.internal.helper;

import lombok.experimental.*;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

@UtilityClass
public final class ByteHelper {
    public static void byteToBytes(byte[] bytes, int offset, byte value) {
        bytes[offset] = value;
    }

    public static byte bytesToByte(byte[] bytes, int offset) {
        return bytes[offset];
    }

    public static void booleanToBytes(byte[] bytes, int offset, boolean value) {
        bytes[offset] = (byte) (value ? 1 : 0);
    }

    public static boolean bytesToBoolean(byte[] bytes, int offset) {
        return bytes[offset] != 0;
    }

    public static void charToBytes(byte[] bytes, int offset, char value) {
        bytes[offset] = (byte) (value >>> 8);
        bytes[offset + 1] = (byte) (value);
    }

    public static char bytesToChar(byte[] bytes, int offset) {
        return (char) ((bytes[offset] << 8) + (bytes[offset + 1] & 0xFF));
    }

    public static void shortToBytes(byte[] bytes, int offset, short value) {
        bytes[offset] = (byte) (value >>> 8);
        bytes[offset + 1] = (byte) (value);
    }

    public static short bytesToShort(byte[] bytes, int offset) {
        return (short) ((bytes[offset] << 8) + (bytes[offset + 1] & 0xFF));
    }

    public static void intToBytes(byte[] bytes, int offset, int value) {
        bytes[offset] = (byte) (value >>> 24);
        bytes[offset + 1] = (byte) (value >>> 16);
        bytes[offset + 2] = (byte) (value >>> 8);
        bytes[offset + 3] = (byte) (value);
    }

    public static int bytesToInt(byte[] bytes, int offset) {
        return (bytes[offset] << 24) + ((bytes[offset + 1] & 0xFF) << 16) + ((bytes[offset + 2] & 0xFF) << 8) + (bytes[offset + 3] & 0xFF);
    }

    public static void longToBytes(byte[] bytes, int offset, long value) {
        bytes[offset] = (byte) (value >> 56);
        bytes[offset + 1] = (byte) (value >>> 48);
        bytes[offset + 2] = (byte) (value >>> 40);
        bytes[offset + 3] = (byte) (value >>> 32);
        bytes[offset + 4] = (byte) (value >>> 24);
        bytes[offset + 5] = (byte) (value >>> 16);
        bytes[offset + 6] = (byte) (value >>> 8);
        bytes[offset + 7] = (byte) (value);
    }

    public static long bytesToLong(byte[] bytes, int offset) {
        return ((long) bytes[offset] << 56) +
               ((long) (bytes[offset + 1] & 0xFF) << 48) +
               ((long) (bytes[offset + 2] & 0xFF) << 40) +
               ((long) (bytes[offset + 3] & 0xFF) << 32) +
               ((long) (bytes[offset + 4] & 0xFF) << 24) +
               ((long) (bytes[offset + 5] & 0xFF) << 16) +
               ((long) (bytes[offset + 6] & 0xFF) << 8) +
               ((long) (bytes[offset + 7] & 0xFF));
    }

    public static void floatToBytes(byte[] bytes, int offset, float value) {
        intToBytes(bytes, offset, Float.floatToIntBits(value));
    }

    public static float bytesToFloat(byte[] bytes, int offset) {
        return Float.intBitsToFloat(bytesToInt(bytes, offset));
    }

    public static void doubleToBytes(byte[] bytes, int offset, double value) {
        longToBytes(bytes, offset, Double.doubleToLongBits(value));
    }

    public static double bytesToDouble(byte[] bytes, int offset) {
        return Double.longBitsToDouble(bytesToLong(bytes, offset));
    }

    public static int sizeBytes(Class<?> type) {
        type = primitiveToBoxed(type);
        if (type == Byte.class)
            return Byte.BYTES;
        if (type == Character.class)
            return Character.BYTES;
        if (type == Boolean.class)
            return 1;
        if (type == Short.class)
            return Short.BYTES;
        if (type == Integer.class)
            return Integer.BYTES;
        if (type == Long.class)
            return Long.BYTES;
        if (type == Float.class)
            return Float.BYTES;
        if (type == Double.class)
            return Double.BYTES;
        if (type == Vector2f.class)
            return 2 * Float.BYTES;
        if (type == Vector3f.class)
            return 3 * Float.BYTES;
        if (type == Vector4f.class)
            return 4 * Float.BYTES;
        throw new IllegalArgumentException("Unknown type: " + type);
    }

    private static Class<?> primitiveToBoxed(Class<?> type) {
        if (type == boolean.class)
            return Boolean.class;
        else if (type == byte.class)
            return Byte.class;
        else if (type == char.class)
            return Character.class;
        else if (type == short.class)
            return Short.class;
        else if (type == int.class)
            return Integer.class;
        else if (type == long.class)
            return Long.class;
        else if (type == float.class)
            return Float.class;
        else if (type == double.class)
            return Double.class;
        else
            return type;
    }
}
