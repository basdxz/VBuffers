package com.github.basdxz.vbuffers.sample;

import com.github.basdxz.vbuffers.layout.Layout;

import static com.github.basdxz.vbuffers.layout.Layout.Attr;
import static com.github.basdxz.vbuffers.old.samples.LayoutA.Stride;
import static com.github.basdxz.vbuffers.sample.XYZBuffer.*;

@Stride({@Attr(name = x, type = Integer.class),
         @Attr(name = y, type = Integer.class),
         @Attr(name = z, type = Integer.class)})
public interface XYZBuffer extends Layout<XYZBuffer> {
    String x = "x";
    String y = "y";
    String z = "z";
}
