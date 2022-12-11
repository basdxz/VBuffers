package com.github.basdxz.vbuffers.copy;

import com.github.basdxz.vbuffers.copy.strategy.CopyStrategy;
import com.github.basdxz.vbuffers.copy.strategy.CrossCopy;
import com.github.basdxz.vbuffers.copy.strategy.MaskedCopy;
import com.github.basdxz.vbuffers.copy.strategy.StraightCopy;
import com.github.basdxz.vbuffers.instance.ExtendedBuffer;
import lombok.*;
import org.jetbrains.annotations.Nullable;

public final class CopyStrategyFactory {
    public static CopyStrategy create(ExtendedBuffer<?> source, ExtendedBuffer<?> target, @Nullable CopyMask mask) {
        val sourceLayoutInfo = source.v$layoutInfo();
        val targetLayoutInfo = target.v$layoutInfo();

        if (mask == null) {
            // Handle unmasked copies
            if (sourceLayoutInfo.equals(targetLayoutInfo))
                return new StraightCopy(source, target);
            return new CrossCopy(source, target);
        } else {
            // Handle masked copies
            if (sourceLayoutInfo.equals(targetLayoutInfo))
                return new MaskedCopy(source, target, mask);
            return new CrossCopy(source, target, mask);
        }
    }
}
