package com.github.basdxz.vbuffers;

import com.github.basdxz.vbuffers.Layout.Attribute;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

@Layout({@Attribute(value = "position", type = Vector3f.class),
         @Attribute(value = "normal", type = Vector3f.class),
         @Attribute(value = "color", type = Vector4f.class),
         @Attribute(value = "texture", type = Vector2f.class)})
public interface LayoutB<LAYOUT extends LayoutA<LAYOUT>> extends VBuffer<LAYOUT> {
    LayoutB<LAYOUT> position(Vector3f value);

    Vector3f position();

    LayoutB<LAYOUT> normal(Vector3f value);

    Vector3f normal();

    LayoutB<LAYOUT> color(Vector4f value);

    Vector4f color();

    LayoutB<LAYOUT> texture(Vector2f value);

    Vector2f texture();
}
