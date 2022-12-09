package com.github.basdxz.vbuffers.samples;

import static com.github.basdxz.vbuffers.access.front.AccessFront.In;
import static com.github.basdxz.vbuffers.access.front.AccessFront.Out;

public interface FrontAccessSimpleSample {
    void set(@In("x") int input);

    @Out("x") int get();
}
