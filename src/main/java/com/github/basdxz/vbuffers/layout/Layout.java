package com.github.basdxz.vbuffers.layout;

import com.github.basdxz.vbuffers.feature.Buffer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface Layout<LAYOUT extends Buffer<LAYOUT>> extends Buffer<LAYOUT> {
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Stride {
        Attribute[] value();
    }

    @Target({})
    @Retention(RetentionPolicy.RUNTIME)
    @interface Attribute {
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
    @interface Chain {
    }
}
