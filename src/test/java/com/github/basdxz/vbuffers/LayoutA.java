package com.github.basdxz.vbuffers;

import com.github.basdxz.vbuffers.Layout.Attribute;

@Layout({@Attribute(value = "position", type = Integer.class),
         @Attribute(value = "normal", type = Integer.class),
         @Attribute(value = "color", type = Integer.class),
         @Attribute(value = "texture", type = Integer.class)})
public interface LayoutA extends VBuffer {
    LayoutA position(int value);

    int position();

    LayoutA normal(int value);

    int normal();

    LayoutA color(int value);

    int color();

    LayoutA texture(int value);

    int texture();
}
