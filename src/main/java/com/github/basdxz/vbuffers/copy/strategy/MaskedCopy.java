package com.github.basdxz.vbuffers.copy.strategy;

import com.github.basdxz.vbuffers.copy.CopyMask;
import com.github.basdxz.vbuffers.instance.ExtendedBuffer;
import com.github.basdxz.vbuffers.layout.Attribute;
import lombok.*;

import java.util.List;

public final class MaskedCopy implements CopyStrategy {
    private final ExtendedBuffer<?> source;
    private final ExtendedBuffer<?> target;
    private final int strideBytes;
    private final List<Attribute> maskedAttributes;

    public MaskedCopy(ExtendedBuffer<?> source, ExtendedBuffer<?> target, CopyMask mask) {
        if (!source.v$layoutInfo().equals(target.v$layoutInfo()))
            throw new IllegalArgumentException("Source and target must have the same layout");

        this.source = source;
        this.target = target;

        val stride = source.v$layoutInfo().stride();
        strideBytes = stride.sizeBytes();

        val sourceAttributes = stride.attributes();
        maskedAttributes = mask.attributeNames().stream()
                               .map(sourceAttributes::get)
                               .toList();
    }

    @Override
    public void copyRange(int sourceIndex, int targetIndex, int length) {
        val sourceBacking = source.v$backing();
        val targetBacking = target.v$backing();

        for (var i = 0; i < length; i++) {
            val sourceOffsetBytes = (sourceIndex + i) * strideBytes;
            val targetOffsetBytes = (targetIndex + i) * strideBytes;

            for (val attribute : maskedAttributes) {
                val attributeSizeBytes = attribute.sizeBytes();
                val attributeOffsetBytes = attribute.offsetBytes();

                targetBacking.put(targetOffsetBytes + attributeOffsetBytes,
                                  sourceBacking,
                                  sourceOffsetBytes + attributeOffsetBytes,
                                  attributeSizeBytes);
            }
        }
    }
}
