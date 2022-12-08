package com.github.basdxz.vbuffers;

import com.github.basdxz.vbuffers.Layout.Attribute;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

@Layout({@Attribute(name = "position", type = Vector3f.class),
         @Attribute(name = "normal", type = Vector3f.class),
         @Attribute(name = "color", type = Vector4f.class),
         @Attribute(name = "texture", type = Vector2f.class)})
public interface LayoutB extends VBuffer<LayoutB> {
    LayoutB position(Vector3f value);

    Vector3f position();

    LayoutB normal(Vector3f value);

    Vector3f normal();

    LayoutB color(Vector4f value);

    Vector4f color();

    LayoutB texture(Vector2f value);

    Vector2f texture();
}
