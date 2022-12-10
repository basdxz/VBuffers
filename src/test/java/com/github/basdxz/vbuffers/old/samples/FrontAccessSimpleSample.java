package com.github.basdxz.vbuffers.old.samples;

import com.github.basdxz.vbuffers.layout.Layout;
import com.github.basdxz.vbuffers.layout.Layout.Attribute;
import com.github.basdxz.vbuffers.layout.Layout.Stride;

import static com.github.basdxz.vbuffers.old.samples.FrontAccessSimpleSample.X;

@Stride(@Attribute(name = X, type = Integer.class))
public interface FrontAccessSimpleSample extends Layout<FrontAccessSimpleSample> {
    String X = "x";

    void set(@In(X) int input);

    @Out(X) int get();
}
