package com.github.basdxz.vbuffers.sample;

import com.github.basdxz.vbuffers.layout.Layout;

import static com.github.basdxz.vbuffers.sample.TestConstants.*;
import static com.github.basdxz.vbuffers.sample.XYZBuffer.Attr;

@Attr(name = X, type = Integer.class)
@Attr(name = Y, type = Integer.class)
@Attr(name = Z, type = Integer.class)
public interface XYZBuffer extends Layout<XYZBuffer> {
    @This XYZBuffer x(@In(X) int value);

    @Out(X) int x();

    @This XYZBuffer y(@In(Y) int value);

    @Out(Y) int y();

    @This XYZBuffer z(@In(Z) int value);

    @Out(Z) int z();
}
