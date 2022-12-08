package com.github.basdxz.vbuffers.helper;

import lombok.experimental.*;

@UtilityClass
public final class BytesConverter {
    public static byte[] byteToBytes(byte value) {
        return new byte[]{value};
    }

    public static byte bytesToByte(byte[] value) {
        return value[0];
    }

    public static byte[] booleanToBytes(boolean value) {
        return new byte[]{(byte) (value ? 1 : 0)};
    }

    public static boolean bytesToBoolean(byte[] value) {
        return value[0] != 0;
    }

    public static byte[] charToBytes(char value) {
        return new byte[]{
                (byte) (value >> 8),
                (byte) (value)
        };
    }

    public static char bytesToChar(byte[] value) {
        return (char) ((value[0] << 8) + (value[1] & 0xFF));
    }

    public static byte[] shortToBytes(short value) {
        return new byte[]{
                (byte) (value >> 8),
                (byte) (value)
        };
    }

    public static short bytesToShort(byte[] value) {
        return (short) ((value[0] << 8) + (value[1] & 0xFF));
    }

    public static byte[] intToBytes(int value) {
        return new byte[]{
                (byte) (value >> 24),
                (byte) (value >> 16),
                (byte) (value >> 8),
                (byte) (value)
        };
    }

    public static int bytesToInt(byte[] value) {
        return (value[0] << 24) + ((value[1] & 0xFF) << 16) + ((value[2] & 0xFF) << 8) + (value[3] & 0xFF);
    }

    public static byte[] longToBytes(long value) {
        return new byte[]{
                (byte) (value >> 56),
                (byte) (value >> 48),
                (byte) (value >> 40),
                (byte) (value >> 32),
                (byte) (value >> 24),
                (byte) (value >> 16),
                (byte) (value >> 8),
                (byte) (value)
        };
    }

    public static long bytesToLong(byte[] value) {
        return ((long) value[0] << 56) +
               ((long) (value[1] & 0xFF) << 48) +
               ((long) (value[2] & 0xFF) << 40) +
               ((long) (value[3] & 0xFF) << 32) +
               ((long) (value[4] & 0xFF) << 24) +
               ((long) (value[5] & 0xFF) << 16) +
               ((long) (value[6] & 0xFF) << 8) +
               ((long) (value[7] & 0xFF));
    }

    public static byte[] floatToBytes(float value) {
        return intToBytes(Float.floatToIntBits(value));
    }

    public static float bytesToFloat(byte[] value) {
        return Float.intBitsToFloat(bytesToInt(value));
    }

    public static byte[] doubleToBytes(double value) {
        return longToBytes(Double.doubleToLongBits(value));
    }

    public static double bytesToDouble(byte[] value) {
        return Double.longBitsToDouble(bytesToLong(value));
    }
}
