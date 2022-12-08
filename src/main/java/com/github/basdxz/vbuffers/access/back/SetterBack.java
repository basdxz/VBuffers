package com.github.basdxz.vbuffers.access.back;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.nio.ByteBuffer;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@FunctionalInterface
public interface SetterBack<T> {
    void put(ByteBuffer buffer, int offsetBytes, T value);

    @Target(METHOD)
    @Retention(RUNTIME)
    @interface Accessor {
        Class<?>[] value();
    }
}