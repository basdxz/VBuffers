package com.github.basdxz.vbuffers.accessor.back.bind;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

// These only have a 'V' prefix to avoid collisions with Lombok's @Getter and @Setter annotations.
public interface BackingAccessorBindings {
    @Target(METHOD)
    @Retention(RUNTIME)
    @interface VSetter {
        Class<?>[] value();
    }

    @Target(METHOD)
    @Retention(RUNTIME)
    @interface VGetter {
        Class<?>[] value();
    }

    @Target(METHOD)
    @Retention(RUNTIME)
    @interface VImmutableGetter {
        Class<?>[] value();
    }
}
