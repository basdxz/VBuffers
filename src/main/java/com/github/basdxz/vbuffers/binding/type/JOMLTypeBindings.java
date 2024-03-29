package com.github.basdxz.vbuffers.binding.type;

import lombok.*;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.nio.ByteBuffer;

//TODO: Complete this for all JOML types
@SuppressWarnings("unused")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JOMLTypeBindings implements TypeBindings {
    @Put(Vector2f.class)
    public static void putVector2f(ByteBuffer buffer, int offsetBytes, Vector2f value) {
        value.get(offsetBytes, buffer);
    }

    @NewGet(Vector2f.class)
    public static Vector2f getVector2f(ByteBuffer buffer, int offsetBytes) {
        return new Vector2f().set(offsetBytes, buffer);
    }

    @Put(Vector3f.class)
    public static void putVector3f(ByteBuffer buffer, int offsetBytes, Vector3f value) {
        value.get(offsetBytes, buffer);
    }

    @NewGet(Vector3f.class)
    public static Vector3f getVector3f(ByteBuffer buffer, int offsetBytes) {
        return new Vector3f().set(offsetBytes, buffer);
    }

    @Put(Vector4f.class)
    public static void putVector4f(ByteBuffer buffer, int offsetBytes, Vector4f value) {
        value.get(offsetBytes, buffer);
    }

    @NewGet(Vector4f.class)
    public static Vector4f getVector4f(ByteBuffer buffer, int offsetBytes) {
        return new Vector4f().set(offsetBytes, buffer);
    }
}
