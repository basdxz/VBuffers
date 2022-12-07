package com.github.basdxz.vbuffers.accessor;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;

@FunctionalInterface
public interface Getter<T> {
    @Contract("_, _, null -> new; _, _, _ -> param3")
    T get(ByteBuffer buffer, int offsetBytes, @Nullable T output);

    default T get(ByteBuffer buffer, int offsetBytes) {
        return get(buffer, offsetBytes, null);
    }
}
