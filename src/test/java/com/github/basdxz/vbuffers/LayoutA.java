package com.github.basdxz.vbuffers;

import com.github.basdxz.vbuffers.Layout.Attribute;

@Layout({@Attribute(value = "position", type = int.class),
         @Attribute(value = "normal", type = int.class),
         @Attribute(value = "color", type = int.class),
         @Attribute(value = "texture", type = int.class)})
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
