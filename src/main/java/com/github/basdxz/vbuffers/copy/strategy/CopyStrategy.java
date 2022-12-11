package com.github.basdxz.vbuffers.copy.strategy;

public sealed interface CopyStrategy permits MaskedCopy, NoCopy, StraightCopy {
    void copyRange(int sourceIndex, int targetIndex, int length);
}
