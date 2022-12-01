package com.github.basdxz.vbuffers;

import lombok.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@NoArgsConstructor
public final class BufferProviderTest {
    private static final int[] SAMPLE_VALUES = new int[]{12};

    @Test
    public void provideLayout() {
        val buffer = BufferProvider.provide(LayoutA.class);
        for (val value : SAMPLE_VALUES) {
            buffer.position(value)
                  .normal(value)
                  .color(value)
                  .texture(value);
            assertEquals(value, buffer.position());
            assertEquals(value, buffer.normal());
            assertEquals(value, buffer.color());
            assertEquals(value, buffer.texture());
        }
    }
}