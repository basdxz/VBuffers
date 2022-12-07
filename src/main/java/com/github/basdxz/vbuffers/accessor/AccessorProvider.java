package com.github.basdxz.vbuffers.accessor;

import java.nio.ByteBuffer;

public class AccessorProvider {
    public static <T> Getter<T> getter(Class<T> type) {
        return (Getter<T>) (buffer, offsetBytes, output) -> (T) ((Object) buffer.getInt(offsetBytes / Integer.BYTES));
    }

    public static <T> Setter<T> setter(Class<T> type) {
        return new Setter<T>() {
            @Override
            public void put(ByteBuffer buffer, int offsetBytes, T value) {
                buffer.putInt(offsetBytes / Integer.BYTES, (Integer) value);
            }
        };
    }
}