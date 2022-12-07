package com.github.basdxz.vbuffers.accessor.impl;

import com.github.basdxz.vbuffers.accessor.Accessor;
import com.github.basdxz.vbuffers.accessor.Accessors;
import lombok.*;

import java.nio.ByteBuffer;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PrimitiveAccessors implements Accessors {
    @Accessor.Setter({byte.class, Byte.class})
    public static void put(ByteBuffer buffer, int offsetBytes, Byte value) {
        buffer.put(offsetBytes, value);
    }

    @Accessor.Getter({byte.class, Byte.class})
    public static byte get(ByteBuffer buffer, int offsetBytes) {
        return buffer.get(offsetBytes);
    }
}
