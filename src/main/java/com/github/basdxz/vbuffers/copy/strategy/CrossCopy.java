package com.github.basdxz.vbuffers.copy.strategy;

import com.github.basdxz.vbuffers.instance.ExtendedBuffer;
import com.github.basdxz.vbuffers.layout.Attribute;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

public final class CrossCopy implements CopyStrategy {
    private final ExtendedBuffer<?> source;
    private final ExtendedBuffer<?> target;
    private final List<AttributeMapping> attributeMappings;

    public CrossCopy(ExtendedBuffer<?> source, ExtendedBuffer<?> target) {
        if (source.v$layoutInfo().equals(target.v$layoutInfo()))
            throw new IllegalArgumentException("Source and target must have different layouts");

        this.source = source;
        this.target = target;

        val attributeMappings = new ArrayList<AttributeMapping>();
        val sourceStride = source.v$layoutInfo().stride();
        val targetStride = target.v$layoutInfo().stride();
        for (val sourceAttribute : sourceStride.attributes().values()) {
            val targetAttribute = targetStride.attributes().get(sourceAttribute.name());
            if (targetAttribute != null)
                attributeMappings.add(new AttributeMapping(sourceAttribute, targetAttribute));
        }

        this.attributeMappings = attributeMappings;
    }

    @Override
    public void copyRange(int sourceIndex, int targetIndex, int length) {
        val sourceBacking = source.v$backing();
        val targetBacking = target.v$backing();

        for (var i = 0; i < length; i++) {
            val sourceOffsetBytes = (sourceIndex + i) * source.v$layoutInfo().stride().sizeBytes();
            val targetOffsetBytes = (targetIndex + i) * target.v$layoutInfo().stride().sizeBytes();

            for (val attributeMapping : attributeMappings) {
                targetBacking.put(targetOffsetBytes + attributeMapping.targetOffsetBytes,
                                  sourceBacking,
                                  sourceOffsetBytes + attributeMapping.sourceOffsetBytes,
                                  attributeMapping.sizeBytes);
            }
        }
    }

    public static class AttributeMapping {
        protected final int sourceOffsetBytes;
        protected final int targetOffsetBytes;
        protected final int sizeBytes;

        public AttributeMapping(Attribute sourceAttribute, Attribute targetAttribute) {
            //Ensure name, class and size are the same
            if (!sourceAttribute.name().equals(targetAttribute.name()))
                throw new IllegalArgumentException("Source and target attribute names must be the same");
            if (!sourceAttribute.type().equals(targetAttribute.type()))
                throw new IllegalArgumentException("Source and target attribute types must be the same");
            if (sourceAttribute.sizeBytes() != targetAttribute.sizeBytes())
                throw new IllegalArgumentException("Source and target attribute sizes must be the same");

            sourceOffsetBytes = sourceAttribute.offsetBytes();
            targetOffsetBytes = targetAttribute.offsetBytes();
            sizeBytes = sourceAttribute.sizeBytes();
        }
    }
}
