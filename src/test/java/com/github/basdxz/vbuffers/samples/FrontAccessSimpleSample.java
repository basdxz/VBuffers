package com.github.basdxz.vbuffers.samples;

import com.github.basdxz.vbuffers.layout.Layout;

import static com.github.basdxz.vbuffers.access.front.AccessFront.In;
import static com.github.basdxz.vbuffers.access.front.AccessFront.Out;
import static com.github.basdxz.vbuffers.samples.FrontAccessSimpleSample.X;

@Layout(@Layout.Attribute(name = X, type = Integer.class))
public interface FrontAccessSimpleSample {
    String X = "x";

    void set(@In(X) int input);

    @Out(X) int get();
}
