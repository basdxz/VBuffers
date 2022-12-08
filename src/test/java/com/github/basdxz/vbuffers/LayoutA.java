package com.github.basdxz.vbuffers;

import com.github.basdxz.vbuffers.Layout.Attribute;

@Layout({@Attribute(value = "position", type = Integer.class, sizeBytes = Integer.BYTES),
         @Attribute(value = "normal", type = Integer.class, sizeBytes = Integer.BYTES),
         @Attribute(value = "color", type = Integer.class, sizeBytes = Integer.BYTES),
         @Attribute(value = "texture", type = Integer.class, sizeBytes = Integer.BYTES)})
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
