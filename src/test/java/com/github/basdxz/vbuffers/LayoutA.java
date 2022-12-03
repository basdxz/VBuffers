package com.github.basdxz.vbuffers;

import com.github.basdxz.vbuffers.Layout.Attribute;

@Layout({@Attribute(value = "positionFixOverlap", type = Integer.class),
         @Attribute(value = "normal", type = Integer.class),
         @Attribute(value = "color", type = Integer.class),
         @Attribute(value = "texture", type = Integer.class)})
public interface LayoutA extends VBuffer {
    LayoutA positionFixOverlap(int value);

    int positionFixOverlap();

    LayoutA normal(int value);

    int normal();

    LayoutA color(int value);

    int color();

    LayoutA texture(int value);

    int texture();
}
