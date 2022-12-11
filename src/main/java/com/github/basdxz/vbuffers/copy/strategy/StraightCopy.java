package com.github.basdxz.vbuffers.copy.strategy;

import com.github.basdxz.vbuffers.instance.ExtendedBuffer;
import lombok.*;

@AllArgsConstructor
public final class StraightCopy implements CopyStrategy {
    private final ExtendedBuffer<?> source;
    private final ExtendedBuffer<?> target;
    private final int strideBytes;

    public StraightCopy(ExtendedBuffer<?> source, ExtendedBuffer<?> target) {
        this.source = source;
        this.target = target;
        this.strideBytes = source.v$layoutInfo().stride().sizeBytes();
    }

    @Override
    public void copyRange(int sourceIndex, int targetIndex, int length) {
        val sourceBacking = source.v$backing();
        val sourceOffsetBytes = sourceIndex * length;

        val targetBacking = target.v$backing();
        val targetOffsetBytes = targetIndex * length;

        val lengthBytes = strideBytes * length;

        targetBacking.put(targetOffsetBytes, sourceBacking, sourceOffsetBytes, lengthBytes);
    }
}
