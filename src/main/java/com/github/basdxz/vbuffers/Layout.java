package com.github.basdxz.vbuffers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Layout {
    Attribute[] value();

    @Target({})
    @Retention(RetentionPolicy.RUNTIME)
    @interface Attribute {
        String name();

        Class<?> type();
    }
}
