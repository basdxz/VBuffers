package com.github.basdxz.vbuffers.copy;

import com.github.basdxz.vbuffers.copy.strategy.CopyStrategy;
import com.github.basdxz.vbuffers.copy.strategy.NoCopy;
import com.github.basdxz.vbuffers.copy.strategy.StraightCopy;
import com.github.basdxz.vbuffers.instance.ExtendedBuffer;
import lombok.*;
import org.jetbrains.annotations.Nullable;

public final class CopyStrategyFactory {
    public static CopyStrategy create(ExtendedBuffer<?> source, ExtendedBuffer<?> target, @Nullable CopyMask mask) {
        val sourceLayoutInfo = source.v$layoutInfo();
        val targetLayoutInfo = target.v$layoutInfo();

        // Handle unmasked copies
        if (mask == null) {
            if (sourceLayoutInfo.equals(targetLayoutInfo))
                return new StraightCopy(source, target);
        }

        // No operation
        return new NoCopy();
    }
}
