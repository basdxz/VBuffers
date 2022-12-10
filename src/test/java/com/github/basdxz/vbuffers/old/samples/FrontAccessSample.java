package com.github.basdxz.vbuffers.old.samples;

import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector3fc;
import org.joml.Vector4fc;

import static com.github.basdxz.vbuffers.layout.Layout.*;

public interface FrontAccessSample {
    // Setters
    void set0(@In("x") Vector2f input);

    void set1(@In("x") Vector2f input, @Idx int offsetStrides);

    @Chain FrontAccessSample set2(@In("x") Vector2f input);

    @Chain FrontAccessSample set3(@In("x") Vector2f input, @Idx int offsetStrides);

    @In("x") Vector2f set4(@In("x") Vector2f input);

    @In("x") Vector2f set5(@In("x") Vector2f input, @Idx int offsetStrides);

    void setVertex(@Idx int offsetStrides,
                   @In("position") Vector4fc position,
                   @In("normal") Vector4fc normal,
                   @In("color") Vector3fc color,
                   @In("texture") Vector2fc texture);

    // Getters
    void get0(@Out("x") Vector2f output);

    void get1(@Out("x") Vector2f output, @Idx int offsetStrides);

    @Chain FrontAccessSample get2(@Out("x") Vector2f output);

    @Chain FrontAccessSample get3(@Out("x") Vector2f output, @Idx int offsetStrides);

    @Out("x") Vector2f get4();

    @Out("x") Vector2f get5(@Idx int offsetStrides);

    @Out("x") Vector2f get6(@Out("x") Vector2f output);

    @Out("x") Vector2f get7(@Out("x") Vector2f output, @Idx int offsetStrides);
}
