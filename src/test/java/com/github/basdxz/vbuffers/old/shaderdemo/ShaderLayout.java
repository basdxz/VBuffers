package com.github.basdxz.vbuffers.old.shaderdemo;

import com.github.basdxz.vbuffers.layout.Layout;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static com.github.basdxz.vbuffers.layout.Layout.Attr;
import static com.github.basdxz.vbuffers.old.shaderdemo.ShaderLayout.COLOR;
import static com.github.basdxz.vbuffers.old.shaderdemo.ShaderLayout.UV;

@Attr(name = UV, type = Vector2f.class)
@Attr(name = COLOR, type = Vector3f.class)
public interface ShaderLayout extends Layout<ShaderLayout> {
    String UV = "uv";
    String COLOR = "color";

    @This ShaderLayout uv(@In(UV) Vector2f value);

    @Out(UV) Vector2f uv();

    @This ShaderLayout color(@In(COLOR) Vector3f value);

    @Out(COLOR) Vector3f color();
}
