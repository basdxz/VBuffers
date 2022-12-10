package com.github.basdxz.vbuffers.binding;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;

@FunctionalInterface
public interface GetterBinding<T> {
    @Contract("_, _, null -> new; _, _, _ -> param3")
    T get(ByteBuffer buffer, int offsetBytes, @Nullable T output);

    @FunctionalInterface
    interface Allocating<T> extends GetterBinding<T> {
        @Override
        default T get(ByteBuffer buffer, int offsetBytes, @Nullable T output) {
            return get(buffer, offsetBytes);
        }

        T get(ByteBuffer buffer, int offsetBytes);
    }
}
