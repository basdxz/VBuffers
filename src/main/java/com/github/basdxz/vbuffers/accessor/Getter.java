package com.github.basdxz.vbuffers.accessor;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.nio.ByteBuffer;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@FunctionalInterface
public interface Getter<T> {
    @Contract("_, _, null -> new; _, _, _ -> param3")
    T get(ByteBuffer buffer, int offsetBytes, @Nullable T output);

    @Target(METHOD)
    @Retention(RUNTIME)
    @interface Accessor {
        Class<?>[] value();
    }

    @FunctionalInterface
    interface Immutable<T> extends Getter<T> {
        @Override
        default T get(ByteBuffer buffer, int offsetBytes, @Nullable T output) {
            return get(buffer, offsetBytes);
        }

        T get(ByteBuffer buffer, int offsetBytes);

        @Target(METHOD)
        @Retention(RUNTIME)
        @interface Accessor {
            Class<?>[] value();
        }
    }
}
