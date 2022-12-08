package com.github.basdxz.vbuffers.accessor.front;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

public interface SetterFront {
    @Target(METHOD)
    @Retention(RUNTIME)
    @interface Access {
        String value() default "";

        Class<? extends SetterFront> type() default SetterFront.class;
    }
}
