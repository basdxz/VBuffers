package com.github.basdxz.vbuffers.shaderdemo;

import com.github.basdxz.vbuffers.VBuffer;
import com.github.basdxz.vbuffers.layout.Layout.Stride;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static com.github.basdxz.vbuffers.layout.Layout.Attribute;

@Stride({@Attribute(name = "uv", type = Vector2f.class),
         @Attribute(name = "color", type = Vector3f.class)})
public interface ShaderLayout extends VBuffer<ShaderLayout> {
    ShaderLayout uv(Vector2f value);

    Vector2f uv();

    ShaderLayout color(Vector3f value);

    Vector3f color();
}
