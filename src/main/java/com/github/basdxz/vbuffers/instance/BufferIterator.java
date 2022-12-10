package com.github.basdxz.vbuffers.instance;

import com.github.basdxz.vbuffers.layout.Layout;
import lombok.*;

import java.util.Iterator;

@AllArgsConstructor
public final class BufferIterator<LAYOUT extends Layout<LAYOUT>> implements Iterator<LAYOUT> {
    private final ExtendedBuffer<LAYOUT> bufferHandler;

    @Override
    public boolean hasNext() {
        return bufferHandler.v$hasRemaining();
    }

    @Override
    public LAYOUT next() {
        // Provides a new instance of the layout which holds a single stride
        return bufferHandler.v$nextStrideView();
    }
}
