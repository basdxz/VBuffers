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
        return bufferHandler.v$nextStrideView();
    }
}
