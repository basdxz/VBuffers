package com.github.basdxz.vbuffers.old.samples;

import com.github.basdxz.vbuffers.layout.Layout;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import static com.github.basdxz.vbuffers.layout.Layout.Attr;
import static com.github.basdxz.vbuffers.old.samples.LayoutB.*;

@Attr(name = POSITION, type = Vector3f.class)
@Attr(name = NORMAL, type = Vector3f.class)
@Attr(name = COLOR, type = Vector4f.class)
@Attr(name = TEXTURE, type = Vector2f.class)
public interface LayoutB extends Layout<LayoutB> {
    String POSITION = "position";
    String NORMAL = "normal";
    String COLOR = "color";
    String TEXTURE = "texture";

    @This LayoutB position(@In(POSITION) Vector3f value);

    @Out(POSITION) Vector3f position();

    @This LayoutB normal(@In(NORMAL) Vector3f value);

    @Out(NORMAL) Vector3f normal();

    @This LayoutB color(@In(COLOR) Vector4f value);

    @Out(COLOR) Vector4f color();

    @This LayoutB texture(@In(TEXTURE) Vector2f value);

    @Out(TEXTURE) Vector2f texture();
}
