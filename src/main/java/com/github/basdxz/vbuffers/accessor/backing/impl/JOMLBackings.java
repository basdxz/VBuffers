package com.github.basdxz.vbuffers.accessor.backing.impl;

import com.github.basdxz.vbuffers.accessor.backing.AccessorBackings;
import com.github.basdxz.vbuffers.accessor.backing.GetterBacking;
import com.github.basdxz.vbuffers.accessor.backing.SetterBacking;
import lombok.*;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.nio.ByteBuffer;

//TODO: Complete this for all JOML types
@SuppressWarnings("unused")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JOMLBackings implements AccessorBackings {
    @SetterBacking.Accessor(Vector2f.class)
    public static void putVector2f(ByteBuffer buffer, int offsetBytes, Vector2f value) {
        value.get(offsetBytes, buffer);
    }

    @GetterBacking.Immutable.Accessor(Vector2f.class)
    public static Vector2f getVector2f(ByteBuffer buffer, int offsetBytes) {
        return new Vector2f().set(offsetBytes, buffer);
    }

    @SetterBacking.Accessor(Vector3f.class)
    public static void putVector3f(ByteBuffer buffer, int offsetBytes, Vector3f value) {
        value.get(offsetBytes, buffer);
    }

    @GetterBacking.Immutable.Accessor(Vector3f.class)
    public static Vector3f getVector3f(ByteBuffer buffer, int offsetBytes) {
        return new Vector3f().set(offsetBytes, buffer);
    }

    @SetterBacking.Accessor(Vector4f.class)
    public static void putVector4f(ByteBuffer buffer, int offsetBytes, Vector4f value) {
        value.get(offsetBytes, buffer);
    }

    @GetterBacking.Immutable.Accessor(Vector4f.class)
    public static Vector4f getVector4f(ByteBuffer buffer, int offsetBytes) {
        return new Vector4f().set(offsetBytes, buffer);
    }
}
