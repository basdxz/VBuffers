package com.github.basdxz.vbuffers;

import lombok.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@NoArgsConstructor
public final class BufferProviderTest {
    private static final int[] SAMPLE_VALUES = new int[]{12, 5000, 389483485};

    @Test
    public void provideLayout() {
        val buffer = BufferProvider.provide(LayoutA.class);
        for (val value : SAMPLE_VALUES) {
            buffer.position(value);
            buffer.normal(value);
            buffer.color(value);
            buffer.texture(value);
            assertEquals(value, buffer.position());
            assertEquals(value, buffer.normal());
            assertEquals(value, buffer.color());
            assertEquals(value, buffer.texture());
        }
    }
}