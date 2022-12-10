package com.github.basdxz.vbuffers.sample;

import com.github.basdxz.vbuffers.layout.Layout;

import static com.github.basdxz.vbuffers.old.samples.LayoutA.Attribute;
import static com.github.basdxz.vbuffers.old.samples.LayoutA.Stride;
import static com.github.basdxz.vbuffers.sample.XYZBuffer.*;

@Stride({@Attribute(name = x, type = Integer.class),
         @Attribute(name = y, type = Integer.class),
         @Attribute(name = z, type = Integer.class)})
public interface XYZBuffer extends Layout<XYZBuffer> {
    String x = "x";
    String y = "y";
    String z = "z";
}
