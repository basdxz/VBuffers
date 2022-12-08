package com.github.basdxz.vbuffers;

import com.github.basdxz.vbuffers.Layout.Attribute;

@Layout({@Attribute(name = "position", type = Integer.class),
         @Attribute(name = "normal", type = Integer.class),
         @Attribute(name = "color", type = Integer.class),
         @Attribute(name = "texture", type = Integer.class)})
public interface LayoutA extends VBuffer<LayoutA> {
    LayoutA position(int value);

    int position();

    LayoutA normal(int value);

    int normal();

    LayoutA color(int value);

    int color();

    LayoutA texture(int value);

    int texture();
}
