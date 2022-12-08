package com.github.basdxz.vbuffers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Layout {
    Attribute[] value();

    int sizeBytes() default -1;

    @Target({})
    @Retention(RetentionPolicy.RUNTIME)
    @interface Attribute {
        String value();

        String[] aliases() default {};

        Class<?> type();

        int sizeBytes();

        int offsetBytes() default -1;

        int orderIndex() default -1;
    }
}
