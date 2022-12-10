package com.github.basdxz.vbuffers.internal.buffer;

import com.github.basdxz.vbuffers.layout.Layout;
import lombok.*;

import java.util.Iterator;

@AllArgsConstructor
public class BufferIterator<LAYOUT extends Layout<LAYOUT>> implements Iterator<LAYOUT> {
    protected final BufferHandler<LAYOUT> bufferHandler;

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
