package com.github.basdxz.vbuffers;

import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.List;
import java.util.function.IntFunction;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

// New buffer of size bigger than 0
// Iterator for read/writing
@NoArgsConstructor
public final class BufferProviderTest {
    private static final int[] SAMPLE_VALUES = new int[]{12, 100, 340, 4300};

    private static final int BUFFER_SIZE = 1000;

    private static final int CONSTANT_A = 1;
    private static final int CONSTANT_B = 2;
    private static final int CONSTANT_C = 3;//make these more interesting numbers

    private static final int SIZE_A = 10;
    private static final int SIZE_B = 20;
    private static final int SIZE_C = 30;

    @Test
    public void test0() {
        val buffer = BufferProvider.newBuffer(LayoutA.class, ByteBuffer::allocate);
        for (val value : SAMPLE_VALUES) {
            val position = value + 55;
            val normal = value - 73;
            val color = value + 81;
            val texture = value + 120;

            //TODO: dumb issue, rename once overlap is solved
            buffer.positionFixOverlap(position)
                  .normal(normal)
                  .color(color)
                  .texture(texture);

            assertEquals(position, buffer.positionFixOverlap());
            assertEquals(normal, buffer.normal());
            assertEquals(color, buffer.color());
            assertEquals(texture, buffer.texture());
        }
    }

    @Test
    public void test1() {
        val intBackingBox = new IntBuffer[1];
        val buffer = BufferProvider.newBuffer(LayoutA.class, capacity -> {
            val allocation = ByteBuffer.allocate(capacity);
            intBackingBox[0] = allocation.asIntBuffer();
            return allocation;
        });
        val intBacking = intBackingBox[0];

        for (val value : SAMPLE_VALUES) {
            val position = value + 55;
            val normal = value - 73;
            val color = value + 81;
            val texture = value + 120;

            //TODO: dumb issue, rename once overlap is solved
            buffer.positionFixOverlap(position)
                  .normal(normal)
                  .color(color)
                  .texture(texture);

            assertEquals(position, intBacking.get(0));
            assertEquals(normal, intBacking.get(1));
            assertEquals(color, intBacking.get(2));
            assertEquals(texture, intBacking.get(3));
        }
    }

    @Test
    public void test2() {
        //Only worked with allocate direct
        val buffer = BufferProvider.newBuffer(LayoutB.class, ByteBuffer::allocateDirect);

        val position = new Vector3f(55F, 994F, -1515F);
        val normal = new Vector3f(35F, 300F, -105F);
        val color = new Vector4f(7777F, 0F, -1F, 1000F);
        val texture = new Vector2f(-642F, 0.66F);

        buffer.positionFixOverlap(position)
              .normal(normal)
              .color(color)
              .texture(texture);

        assertEquals(position, buffer.positionFixOverlap());
        assertEquals(normal, buffer.normal());
        assertEquals(color, buffer.color());
        assertEquals(texture, buffer.texture());
    }

    // Buffer treated as an array for reads and writes
    // https://www.baeldung.com/java-bytebuffer
    @Test
    public void test3() {
        val buffer = BufferProvider.newBuffer(LayoutB.class, ByteBuffer::allocateDirect, BUFFER_SIZE);
        val positions = newRangeList(BUFFER_SIZE, Vector3f::new);
        val normals = newRangeList(BUFFER_SIZE, Vector3f::new);
        val colors = IntStream.range(0, BUFFER_SIZE)
                              .mapToObj(Vector4f::new)
                              .toList();
        val textures = IntStream.range(0, BUFFER_SIZE)
                                .mapToObj(Vector2f::new)
                                .toList();

        while (buffer.hasRemaining()) {
            val index = buffer.position();
            buffer.positionFixOverlap(positions.get(index))
                  .normal(normals.get(index))
                  .color(colors.get(index))
                  .texture(textures.get(index));
            buffer.position(index + 1);
        }
        buffer.clear();

        while (buffer.hasRemaining()) {
            val index = buffer.position();
            assertEquals(positions.get(index), buffer.positionFixOverlap());
            assertEquals(normals.get(index), buffer.normal());
            assertEquals(colors.get(index), buffer.color());
            assertEquals(textures.get(index), buffer.texture());
            buffer.position(index + 1);
        }
    }

    // Flip/Compact demonstration
    // https://www.happycoders.eu/java/bytebuffer-flip-compact
    @Test
    public void test4() {
        val middleOfB = SIZE_B / 2;

        // Create the test buffer
        val buffer = BufferProvider.newBuffer(LayoutA.class, ByteBuffer::allocateDirect, BUFFER_SIZE);

        // Write constant A to the buffer SIZE_A times
        for (var i = 0; i < SIZE_A; i++) {
            buffer.positionFixOverlap(CONSTANT_A)
                  .normal(CONSTANT_A)
                  .color(CONSTANT_A)
                  .texture(CONSTANT_A);
            buffer.next();
        }
        // Write constant B to the buffer SIZE_B times
        for (var i = 0; i < SIZE_B; i++) {
            buffer.positionFixOverlap(2)
                  .normal(2)
                  .color(2)
                  .texture(2);
            buffer.next();
        }
        // Flip the buffer
        buffer.flip();

        // Read the CONSTANT_A values from the buffer
        for (var i = 0; i < SIZE_A; i++) {
            assertEquals(CONSTANT_A, buffer.positionFixOverlap());
            assertEquals(CONSTANT_A, buffer.normal());
            assertEquals(CONSTANT_A, buffer.color());
            assertEquals(CONSTANT_A, buffer.texture());
            buffer.next();
        }
        // Read half of the CONSTANT_B values from the buffer
        for (var i = 0; i < middleOfB; i++) {
            assertEquals(CONSTANT_B, buffer.positionFixOverlap());
            assertEquals(CONSTANT_B, buffer.normal());
            assertEquals(CONSTANT_B, buffer.color());
            assertEquals(CONSTANT_B, buffer.texture());
            buffer.next();
        }
        // Compact the buffer
        buffer.compact();

        // Write constant C to the buffer SIZE_C times
        for (var i = 0; i < SIZE_C; i++) {
            buffer.positionFixOverlap(CONSTANT_C)
                  .normal(CONSTANT_C)
                  .color(CONSTANT_C)
                  .texture(CONSTANT_C);
            buffer.next();
        }
        buffer.flip();

        // Read the rest of the CONSTANT_B values from the buffer
        for (var i = 0; i < middleOfB; i++) {
            assertEquals(CONSTANT_B, buffer.positionFixOverlap());
            assertEquals(CONSTANT_B, buffer.normal());
            assertEquals(CONSTANT_B, buffer.color());
            assertEquals(CONSTANT_B, buffer.texture());
            buffer.next();
        }
        // Read the CONSTANT_C values from the buffer
        for (var i = 0; i < SIZE_C; i++) {
            assertEquals(CONSTANT_C, buffer.positionFixOverlap());
            assertEquals(CONSTANT_C, buffer.normal());
            assertEquals(CONSTANT_C, buffer.color());
            assertEquals(CONSTANT_C, buffer.texture());
            buffer.next();
        }
    }

    @Test
    public void testCopy() {
        // Create the test buffer
        val buffer = BufferProvider.newBuffer(LayoutB.class, ByteBuffer::allocateDirect, 10);

        //  Create the test data
        val position = new Vector3f(55F, 994F, -1515F);
        val normal = new Vector3f(35F, 300F, -105F);
        val color = new Vector4f(7777F, 0F, -1F, 1000F);
        val texture = new Vector2f(-642F, 0.66F);

        // Write the test data at position 3 in the buffer
        buffer.position(3);
        buffer.positionFixOverlap(position)
              .normal(normal)
              .color(color)
              .texture(texture);

        // Copy the test data to position 9 in the buffer
        buffer.copy(3, 9);

        // Read the test data from position 9 in the buffer
        buffer.position(9);
        assertEquals(position, buffer.positionFixOverlap());
        assertEquals(normal, buffer.normal());
        assertEquals(color, buffer.color());
        assertEquals(texture, buffer.texture());
    }

    // Duplicate
    @Test
    public void test5() {
        val buffer = BufferProvider.newBuffer(LayoutB.class, ByteBuffer::allocateDirect, 10);
    }

    // Slice
    @Test
    public void test6() {
        val buffer = BufferProvider.newBuffer(LayoutB.class, ByteBuffer::allocateDirect, 10);
    }

    // Read-Only
    @Test
    public void test7() {
        val buffer = BufferProvider.newBuffer(LayoutB.class, ByteBuffer::allocateDirect, 10);
    }

    // Iterate
    @Test
    public void test8() {
        val buffer = BufferProvider.newBuffer(LayoutB.class, ByteBuffer::allocateDirect, 10);
    }

    // Stream
    @Test
    public void test9() {
        val buffer = BufferProvider.newBuffer(LayoutB.class, ByteBuffer::allocateDirect, 10);
    }

    // Buffer to Buffer transfers
    @Test
    public void test10() {
        val buffer = BufferProvider.newBuffer(LayoutB.class, ByteBuffer::allocateDirect, 10);
    }

    @NotNull
    private static <T> List<T> newRangeList(int size, IntFunction<T> mapper) {
        return newRangeList(0, size, mapper);
    }

    @NotNull
    private static <T> List<T> newRangeList(int start, int size, IntFunction<T> mapper) {
        return IntStream.range(start, start + size)
                        .mapToObj(mapper)
                        .toList();
    }
}