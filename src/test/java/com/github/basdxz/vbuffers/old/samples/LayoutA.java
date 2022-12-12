package com.github.basdxz.vbuffers.old.samples;

import com.github.basdxz.vbuffers.layout.Layout;

import static com.github.basdxz.vbuffers.layout.Layout.Attr;
import static com.github.basdxz.vbuffers.old.samples.LayoutA.*;

@Attr(name = POSITION, type = Integer.class)
@Attr(name = NORMAL, type = Integer.class)
@Attr(name = COLOR, type = Integer.class)
@Attr(name = TEXTURE, type = Integer.class)
public interface LayoutA extends Layout<LayoutA> {
    String POSITION = "position";
    String NORMAL = "normal";
    String COLOR = "color";
    String TEXTURE = "texture";

    @This LayoutA position(@In(POSITION) int value);

    @Out(POSITION) int position();

    @This LayoutA normal(@In(NORMAL) int value);

    @Out(NORMAL) int normal();

    @This LayoutA color(@In(COLOR) int value);

    @Out(COLOR) int color();

    @This LayoutA texture(@In(TEXTURE) int value);

    @Out(TEXTURE) int texture();
}
