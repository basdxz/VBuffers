package com.github.basdxz.vbuffers;

import lombok.*;

import java.util.Iterator;

@AllArgsConstructor
public class VBufferIterator<LAYOUT extends VBuffer<LAYOUT>> implements Iterator<LAYOUT> {
    protected final VBufferHandler<LAYOUT> bufferHandler;

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
