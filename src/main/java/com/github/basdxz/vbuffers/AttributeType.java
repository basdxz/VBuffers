package com.github.basdxz.vbuffers;

import lombok.*;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public interface AttributeType {
    Map<Class<?>, AttributeType> DEFAULT_ATTRIBUTE_TYPES = newDefaultAttributes();

    Class<?> type();

    int sizeBytes();

    void set(ByteBuffer buffer, int offsetBytes, Object value);

    Object get(ByteBuffer buffer, int offsetBytes);

    static Map<Class<?>, AttributeType> newDefaultAttributes() {
        val defaultAttributes = new HashMap<Class<?>, AttributeType>();
        val integerType = new IntegerType();
        defaultAttributes.put(integerType.type(), integerType);
        val vec2Type = new Vector2fType();
        defaultAttributes.put(vec2Type.type(), vec2Type);
        val vec3Type = new Vector3fType();
        defaultAttributes.put(vec3Type.type(), vec3Type);
        val vec4Type = new Vector4fType();
        defaultAttributes.put(vec4Type.type(), vec4Type);
        return defaultAttributes;
    }

    @NoArgsConstructor
    class IntegerType implements AttributeType {
        @Override
        public Class<?> type() {
            return Integer.class;
        }

        @Override
        public int sizeBytes() {
            return Integer.BYTES;
        }

        @Override
        public void set(ByteBuffer buffer, int offsetBytes, Object value) {
            if (!(value instanceof Integer))
                throw new RuntimeException("oh no");
            buffer.putInt(offsetBytes, (Integer) value);
        }

        @Override
        public Object get(ByteBuffer buffer, int offsetBytes) {
            return buffer.getInt(offsetBytes);
        }
    }

    @NoArgsConstructor
    class Vector2fType implements AttributeType {
        @Override
        public Class<?> type() {
            return Vector2f.class;
        }

        @Override
        public int sizeBytes() {
            return Float.BYTES * 2;
        }

        @Override
        public void set(ByteBuffer buffer, int offsetBytes, Object value) {
            ((Vector2f) value).get(offsetBytes, buffer);
        }

        @Override
        public Object get(ByteBuffer buffer, int offsetBytes) {
            return new Vector2f().set(offsetBytes, buffer);
        }
    }

    @NoArgsConstructor
    class Vector3fType implements AttributeType {
        @Override
        public Class<?> type() {
            return Vector3f.class;
        }

        @Override
        public int sizeBytes() {
            return Float.BYTES * 3;
        }

        @Override
        public void set(ByteBuffer buffer, int offsetBytes, Object value) {
            ((Vector3f) value).get(offsetBytes, buffer);
        }

        @Override
        public Object get(ByteBuffer buffer, int offsetBytes) {
            return new Vector3f().set(offsetBytes, buffer);
        }
    }

    @NoArgsConstructor
    class Vector4fType implements AttributeType {
        @Override
        public Class<?> type() {
            return Vector4f.class;
        }

        @Override
        public int sizeBytes() {
            return Float.BYTES * 4;
        }

        @Override
        public void set(ByteBuffer buffer, int offsetBytes, Object value) {
            ((Vector4f) value).get(offsetBytes, buffer);
        }

        @Override
        public Object get(ByteBuffer buffer, int offsetBytes) {
            return new Vector4f().set(offsetBytes, buffer);
        }
    }
}
