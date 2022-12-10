package com.github.basdxz.vbuffers.sample;

import com.github.basdxz.vbuffers.layout.Layout;

import static com.github.basdxz.vbuffers.sample.XYZBuffer.*;

@Stride({@Attr(name = x, type = Integer.class),
         @Attr(name = y, type = Integer.class),
         @Attr(name = z, type = Integer.class)})
public interface XYZBuffer extends Layout<XYZBuffer> {
    String x = "x";
    String y = "y";
    String z = "z";

    @This XYZBuffer x(@In(x) int value);

    @Out(x) int x();

    @This XYZBuffer y(@In(y) int value);

    @Out(y) int y();

    @This XYZBuffer z(@In(z) int value);

    @Out(z) int z();
}
