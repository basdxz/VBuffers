package com.github.basdxz.vbuffers.layout;

import com.github.basdxz.vbuffers.instance.ExtendedBuffer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface Layout<LAYOUT extends ExtendedBuffer<LAYOUT>> extends ExtendedBuffer<LAYOUT> {
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Stride {
        Attr[] value();
    }

    @Target({})
    @Retention(RetentionPolicy.RUNTIME)
    @interface Attr {
        String name();

        Class<?> type();
    }

    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Idx {
    }

    @Target(ElementType.TYPE_USE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface In {
        String value();
    }

    @Target(ElementType.TYPE_USE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Out {
        String value();
    }

    @Target(ElementType.TYPE_USE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface This {
    }
}
