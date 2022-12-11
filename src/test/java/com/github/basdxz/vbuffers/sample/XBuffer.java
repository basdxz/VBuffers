package com.github.basdxz.vbuffers.sample;

import com.github.basdxz.vbuffers.layout.Layout;

import static com.github.basdxz.vbuffers.sample.TestConstants.X;
import static com.github.basdxz.vbuffers.sample.XYZBuffer.Attr;
import static com.github.basdxz.vbuffers.sample.XYZBuffer.Stride;

@Stride(@Attr(name = X, type = Integer.class))
public interface XBuffer extends Layout<XBuffer> {
    @This XBuffer x(@In(X) int value);

    @Out(X) int x();
}
