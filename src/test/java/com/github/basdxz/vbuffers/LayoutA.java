package com.github.basdxz.vbuffers;

import com.github.basdxz.vbuffers.Layout.Attribute;

@Layout({@Attribute("position"),
         @Attribute("normal"),
         @Attribute("color"),
         @Attribute("texture")})
public interface LayoutA extends VBuffer {
    void position(int value);

    int position();

    void normal(int value);

    int normal();

    void color(int value);

    int color();

    void texture(int value);

    int texture();
}
