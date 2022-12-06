package com.github.basdxz.vbuffers.shaderdemo;

import com.github.basdxz.vbuffers.Layout;
import com.github.basdxz.vbuffers.Layout.Attribute;
import com.github.basdxz.vbuffers.VBuffer;
import org.joml.Vector2f;
import org.joml.Vector3f;

@Layout({@Attribute(value = "uv", type = Vector2f.class),
         @Attribute(value = "color", type = Vector3f.class)})
public interface ShaderLayout extends VBuffer<ShaderLayout> {
    ShaderLayout uv(Vector2f value);

    Vector2f uv();

    ShaderLayout color(Vector3f value);

    Vector3f color();
}