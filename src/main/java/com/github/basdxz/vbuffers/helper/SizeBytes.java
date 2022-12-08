package com.github.basdxz.vbuffers.helper;

import lombok.experimental.*;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

@UtilityClass
public final class SizeBytes {
    public static int sizeBytes(Class<?> type) {
        type = boxed(type);
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

    private static Class<?> boxed(Class<?> type) {
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
