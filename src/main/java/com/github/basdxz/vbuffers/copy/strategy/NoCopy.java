package com.github.basdxz.vbuffers.copy.strategy;

public final class NoCopy implements CopyStrategy {
    @Override
    public void copyRange(int sourceIndex, int targetIndex, int length) {
    }
}
