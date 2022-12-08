package com.github.basdxz.vbuffers.access.front;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

public interface GetterFront {
    boolean isSupported(Method method);


    @Target(METHOD)
    @Retention(RUNTIME)
    @interface Access {
        String value() default "";

        Class<? extends GetterFront> type() default GetterFront.class;
    }
}
