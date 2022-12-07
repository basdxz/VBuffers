package com.github.basdxz.vbuffers.accessor.impl;

import com.github.basdxz.vbuffers.accessor.Accessors;
import com.github.basdxz.vbuffers.accessor.Getter;
import com.github.basdxz.vbuffers.accessor.Setter;
import lombok.*;

import java.nio.ByteBuffer;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PrimitiveAccessors implements Accessors {
    @Setter.Accessor({byte.class, Byte.class})
    public static void putByte(ByteBuffer buffer, int offsetBytes, Byte value) {
        buffer.put(offsetBytes, value);
    }

    @Getter.Immutable.Accessor({byte.class, Byte.class})
    public static byte getByte(ByteBuffer buffer, int offsetBytes) {
        return buffer.get(offsetBytes);
    }

    @Setter.Accessor({char.class, Character.class})
    public static void putChar(ByteBuffer buffer, int offsetBytes, Character value) {
        buffer.putChar(offsetBytes, value);
    }

    @Getter.Immutable.Accessor({char.class, Character.class})
    public static char getChar(ByteBuffer buffer, int offsetBytes) {
        return buffer.getChar(offsetBytes);
    }

    @Setter.Accessor({short.class, Short.class})
    public static void putShort(ByteBuffer buffer, int offsetBytes, Short value) {
        buffer.putShort(offsetBytes, value);
    }

    @Getter.Immutable.Accessor({short.class, Short.class})
    public static short getShort(ByteBuffer buffer, int offsetBytes) {
        return buffer.getShort(offsetBytes);
    }

    @Setter.Accessor({int.class, Integer.class})
    public static void putInt(ByteBuffer buffer, int offsetBytes, Integer value) {
        buffer.putInt(offsetBytes, value);
    }

    @Getter.Immutable.Accessor({int.class, Integer.class})
    public static int getInt(ByteBuffer buffer, int offsetBytes) {
        return buffer.getInt(offsetBytes);
    }

    @Setter.Accessor({long.class, Long.class})
    public static void putLong(ByteBuffer buffer, int offsetBytes, Long value) {
        buffer.putLong(offsetBytes, value);
    }

    @Getter.Immutable.Accessor({long.class, Long.class})
    public static long getLong(ByteBuffer buffer, int offsetBytes) {
        return buffer.getLong(offsetBytes);
    }

    @Setter.Accessor({float.class, Float.class})
    public static void putFloat(ByteBuffer buffer, int offsetBytes, Float value) {
        buffer.putFloat(offsetBytes, value);
    }

    @Getter.Immutable.Accessor({float.class, Float.class})
    public static float getFloat(ByteBuffer buffer, int offsetBytes) {
        return buffer.getFloat(offsetBytes);
    }

    @Setter.Accessor({double.class, Double.class})
    public static void putDouble(ByteBuffer buffer, int offsetBytes, Double value) {
        buffer.putDouble(offsetBytes, value);
    }

    @Getter.Immutable.Accessor({double.class, Double.class})
    public static double getDouble(ByteBuffer buffer, int offsetBytes) {
        return buffer.getDouble(offsetBytes);
    }

    @Setter.Accessor({boolean.class, Boolean.class})
    public static void putBoolean(ByteBuffer buffer, int offsetBytes, Boolean value) {
        buffer.put(offsetBytes, (byte) (value ? 1 : 0));
    }

    @Getter.Immutable.Accessor({boolean.class, Boolean.class})
    public static boolean getBoolean(ByteBuffer buffer, int offsetBytes) {
        return buffer.get(offsetBytes) != 0;
    }
}
