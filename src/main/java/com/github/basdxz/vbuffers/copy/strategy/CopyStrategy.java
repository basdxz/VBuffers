package com.github.basdxz.vbuffers.copy.strategy;

public sealed interface CopyStrategy permits CrossCopy, MaskedCopy, NoCopy, StraightCopy {
    void copyRange(int sourceIndex, int targetIndex, int length);
}
