package com.github.basdxz.vbuffers;

import lombok.*;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.assertEquals;

@NoArgsConstructor
public final class BufferProviderTest {
    private static final int[] SAMPLE_VALUES = new int[]{12};

    @Test
    public void test0() {
        val backing = ByteBuffer.allocate(4 * 4);

        val buffer = BufferProvider.newBuffer(LayoutA.class, backing);
        for (val value : SAMPLE_VALUES) {
            val position = value + 55;
            val normal = value + 55;
            val color = value + 55;
            val texture = value + 55;

            buffer.position(position)
                  .normal(normal)
                  .color(color)
                  .texture(texture);

            assertEquals(position, buffer.position());
            assertEquals(normal, buffer.normal());
            assertEquals(color, buffer.color());
            assertEquals(texture, buffer.texture());
        }
    }

    @Test
    public void test1() {
        val backing = ByteBuffer.allocate(4 * 4);
        val buffer = BufferProvider.newBuffer(LayoutA.class, backing);

        val intBacking = backing.asIntBuffer();
        for (val value : SAMPLE_VALUES) {
            buffer.position(value)
                  .normal(value)
                  .color(value)
                  .texture(value);
            assertEquals(value, intBacking.get(0));
            assertEquals(value, intBacking.get(1));
            assertEquals(value, intBacking.get(2));
            assertEquals(value, intBacking.get(3));
        }
    }
}