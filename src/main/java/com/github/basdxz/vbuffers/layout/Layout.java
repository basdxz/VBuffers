package com.github.basdxz.vbuffers.layout;

import com.github.basdxz.vbuffers.instance.ExtendedBuffer;

import java.lang.annotation.*;

public interface Layout<LAYOUT extends ExtendedBuffer<LAYOUT>> extends ExtendedBuffer<LAYOUT> {
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Stride {
        Attr[] value();
    }

    //NOTE: Repeatable only kicks in if the total number of Attrs is greater than 1
    @Repeatable(Stride.class)
    @Target(ElementType.TYPE)
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
