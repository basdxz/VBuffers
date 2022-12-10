package com.github.basdxz.vbuffers.old.samples;

import com.github.basdxz.vbuffers.layout.Layout;

import static com.github.basdxz.vbuffers.layout.Layout.Attr;
import static com.github.basdxz.vbuffers.layout.Layout.Stride;
import static com.github.basdxz.vbuffers.old.samples.LayoutA.*;

@Stride({@Attr(name = POSITION, type = Integer.class),
         @Attr(name = NORMAL, type = Integer.class),
         @Attr(name = COLOR, type = Integer.class),
         @Attr(name = TEXTURE, type = Integer.class)})
public interface LayoutA extends Layout<LayoutA> {
    String POSITION = "position";
    String NORMAL = "normal";
    String COLOR = "color";
    String TEXTURE = "texture";

    @Chain LayoutA position(@In(POSITION) int value);

    @Out(POSITION) int position();

    @Chain LayoutA normal(@In(NORMAL) int value);

    @Out(NORMAL) int normal();

    @Chain LayoutA color(@In(COLOR) int value);

    @Out(COLOR) int color();

    @Chain LayoutA texture(@In(TEXTURE) int value);

    @Out(TEXTURE) int texture();
}
