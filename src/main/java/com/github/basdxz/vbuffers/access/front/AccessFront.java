package com.github.basdxz.vbuffers.access.front;

@FunctionalInterface
public interface AccessFront {
    Object accept(Object... args) throws Throwable;
}
