package com.github.basdxz.vbuffers.instance;

import com.github.basdxz.vbuffers.layout.LayoutInfo;
import org.jetbrains.annotations.Contract;

@SuppressWarnings("unused")
public interface Buffer<LAYOUT extends Buffer<LAYOUT>> {
    String INTERNAL_METHOD_PREFIX = "v$";

    @Contract(pure = true)
    LayoutInfo<LAYOUT> v$layoutInfo();
}
