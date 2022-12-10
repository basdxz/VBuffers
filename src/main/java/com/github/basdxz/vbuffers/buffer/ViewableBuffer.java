package com.github.basdxz.vbuffers.buffer;

import org.jetbrains.annotations.Contract;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public interface ViewableBuffer<LAYOUT extends ViewableBuffer<LAYOUT>> extends Buffer<LAYOUT> {
    @Contract(value = "-> new", pure = true)
    LAYOUT v$duplicateView();

    @Contract(value = "-> new", pure = true)
    LAYOUT v$nextStrideView();

    @Contract(value = "-> new", pure = true)
    LAYOUT v$strideView();

    @Contract(value = "_ -> new", pure = true)
    LAYOUT v$strideView(int index);

    @Contract(value = "-> new", pure = true)
    LAYOUT v$sliceView();

    @Contract(value = "_, _-> new", pure = true)
    LAYOUT v$sliceView(int startIndex, int length);

    @Contract(value = "-> new", pure = true)
    LAYOUT v$asReadOnlyView();
}
