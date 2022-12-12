package com.github.basdxz.vbuffers.sample;

import com.github.basdxz.vbuffers.layout.Layout;

import static com.github.basdxz.vbuffers.sample.TestConstants.*;
import static com.github.basdxz.vbuffers.sample.XYZBuffer.Attr;

@Attr(name = Z, type = Integer.class)
@Attr(name = Y, type = Integer.class)
@Attr(name = X, type = Integer.class)
public interface ZYXBuffer extends Layout<ZYXBuffer> {
    @This ZYXBuffer z(@In(Z) int value);

    @Out(Z) int z();

    @This ZYXBuffer y(@In(Y) int value);

    @Out(Y) int y();

    @This ZYXBuffer x(@In(X) int value);

    @Out(X) int x();
}
