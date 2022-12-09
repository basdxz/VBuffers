package com.github.basdxz.vbuffers;

import com.github.basdxz.vbuffers.layout.Layout;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import static com.github.basdxz.vbuffers.LayoutB.*;
import static com.github.basdxz.vbuffers.access.front.AccessFront.*;
import static com.github.basdxz.vbuffers.layout.Layout.Attribute;

@Layout({@Attribute(name = POSITION, type = Vector3f.class),
         @Attribute(name = NORMAL, type = Vector3f.class),
         @Attribute(name = COLOR, type = Vector4f.class),
         @Attribute(name = TEXTURE, type = Vector2f.class)})
public interface LayoutB extends VBuffer<LayoutB> {
    String POSITION = "position";
    String NORMAL = "normal";
    String COLOR = "color";
    String TEXTURE = "texture";

    @Chain LayoutB position(@In(POSITION) Vector3f value);

    @Out(POSITION) Vector3f position();

    @Chain LayoutB normal(@In(NORMAL) Vector3f value);

    @Out(NORMAL) Vector3f normal();

    @Chain LayoutB color(@In(COLOR) Vector4f value);

    @Out(COLOR) Vector4f color();

    @Chain LayoutB texture(@In(TEXTURE) Vector2f value);

    @Out(TEXTURE) Vector2f texture();
}
