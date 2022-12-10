package com.github.basdxz.vbuffers.binding;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

public interface Bindings {
    @Target(METHOD)
    @Retention(RUNTIME)
    @interface Put {
        Class<?>[] value();
    }

    @Target(METHOD)
    @Retention(RUNTIME)
    @interface Get {
        Class<?>[] value();
    }

    @Target(METHOD)
    @Retention(RUNTIME)
    @interface ImGet {
        Class<?>[] value();
    }
}
