package com.github.basdxz.vbuffers;

import lombok.*;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ReadOnlyBufferException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@NoArgsConstructor
public final class VBufferTest {
    private static final int[] SAMPLE_VALUES = new int[]{12, 100, 340, 4300};

    private static final int SIZE_A = 10;
    private static final int SIZE_B = 20;
    private static final int SIZE_C = 30;
    private static final int SIZE_D = 1000;

    @Test
    @DisplayName("Simple I/O")
    public void singleReadWrite() {
        val buffer = VBufferHandler.newBuffer(LayoutA.class, ByteBuffer::allocate);
        for (val value : SAMPLE_VALUES) {
            val position = value + 55;
            val normal = value - 73;
            val color = value + 81;
            val texture = value + 120;

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
    @DisplayName("Simple I/O with ByteBuffer interop")
    public void directBackingReadWrite() {
        val intBackingBox = new IntBuffer[1];
        val buffer = VBufferHandler.newBuffer(LayoutA.class, capacity -> {
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

            buffer.position(position)
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
    @DisplayName("JOML interop")
    public void basicJOMLVectors() {
        val buffer = VBufferHandler.newBuffer(LayoutB.class, ByteBuffer::allocateDirect);

        val position = new Vector3f(55F, 994F, -1515F);
        val normal = new Vector3f(35F, 300F, -105F);
        val color = new Vector4f(7777F, 0F, -1F, 1000F);
        val texture = new Vector2f(-642F, 0.66F);

        buffer.position(position)
              .normal(normal)
              .color(color)
              .texture(texture);

        assertEquals(position, buffer.position());
        assertEquals(normal, buffer.normal());
        assertEquals(color, buffer.color());
        assertEquals(texture, buffer.texture());
    }

    @Test
    @DisplayName("Multiple strides per buffer")
    public void readWriteStrides() {
        val buffer = VBufferHandler.newBuffer(LayoutB.class, ByteBuffer::allocateDirect, SIZE_D);
        val positions = IntStream.range(0, SIZE_D)
                                 .mapToObj(Vector3f::new)
                                 .toList();
        val normals = IntStream.range(0, SIZE_D)
                               .mapToObj(Vector3f::new)
                               .toList();
        val colors = IntStream.range(0, SIZE_D)
                              .mapToObj(Vector4f::new)
                              .toList();
        val textures = IntStream.range(0, SIZE_D)
                                .mapToObj(Vector2f::new)
                                .toList();

        while (buffer.v$hasRemaining()) {
            val index = buffer.v$position();
            buffer.position(positions.get(index))
                  .normal(normals.get(index))
                  .color(colors.get(index))
                  .texture(textures.get(index));
            buffer.v$next();
        }
        buffer.v$clear();

        while (buffer.v$hasRemaining()) {
            val index = buffer.v$position();
            assertEquals(positions.get(index), buffer.position());
            assertEquals(normals.get(index), buffer.normal());
            assertEquals(colors.get(index), buffer.color());
            assertEquals(textures.get(index), buffer.texture());
            buffer.v$next();
        }
    }

    @Test
    @DisplayName("ByteBuffer-like API for navigation")
    public void navigation() {
        //TODO: test navigation
    }

    @Test
    @DisplayName("ByteBuffer-like API for flip and compact")
    public void flipAndCompact() {
        val middleOfB = SIZE_B / 2;
        val buffer = VBufferHandler.newBuffer(LayoutA.class, ByteBuffer::allocateDirect, SIZE_D);

        // Write 1s
        for (var i = 0; i < SIZE_A; i++) {
            buffer.position(1)
                  .normal(1)
                  .color(1)
                  .texture(1);
            buffer.v$next();
        }
        // Write 2s
        for (var i = 0; i < SIZE_B; i++) {
            buffer.position(2)
                  .normal(2)
                  .color(2)
                  .texture(2);
            buffer.v$next();
        }
        // Flip
        buffer.v$flip();

        // Read 1s
        for (var i = 0; i < SIZE_A; i++) {
            assertEquals(1, buffer.position());
            assertEquals(1, buffer.normal());
            assertEquals(1, buffer.color());
            assertEquals(1, buffer.texture());
            buffer.v$next();
        }
        // Read half of the 2s
        for (var i = 0; i < middleOfB; i++) {
            assertEquals(2, buffer.position());
            assertEquals(2, buffer.normal());
            assertEquals(2, buffer.color());
            assertEquals(2, buffer.texture());
            buffer.v$next();
        }
        // Compact
        buffer.v$compact();

        // Write 3s
        for (var i = 0; i < SIZE_C; i++) {
            buffer.position(3)
                  .normal(3)
                  .color(3)
                  .texture(3);
            buffer.v$next();
        }
        // Flip
        buffer.v$flip();

        // Read the rest of the 2s
        for (var i = 0; i < middleOfB; i++) {
            assertEquals(2, buffer.position());
            assertEquals(2, buffer.normal());
            assertEquals(2, buffer.color());
            assertEquals(2, buffer.texture());
            buffer.v$next();
        }
        // Read the 3s
        for (var i = 0; i < SIZE_C; i++) {
            assertEquals(3, buffer.position());
            assertEquals(3, buffer.normal());
            assertEquals(3, buffer.color());
            assertEquals(3, buffer.texture());
            buffer.v$next();
        }
    }

    @Test
    @DisplayName("ByteBuffer-like API for duplicate views")
    public void duplicate() {
        val buffer = VBufferHandler.newBuffer(LayoutA.class, ByteBuffer::allocateDirect, SIZE_A);

        // Fill buffer
        for (var i = 0; i < SIZE_A; i++) {
            buffer.position(i)
                  .normal(i)
                  .color(i)
                  .texture(i);
            buffer.v$next();
        }

        // Read the duplicate buffer
        val duplicateBuffer = buffer.v$duplicateView();
        duplicateBuffer.v$flip();
        for (var i = 0; i < SIZE_A; i++) {
            assertEquals(i, duplicateBuffer.position());
            assertEquals(i, duplicateBuffer.normal());
            assertEquals(i, duplicateBuffer.color());
            assertEquals(i, duplicateBuffer.texture());
            duplicateBuffer.v$next();
        }
    }

    @Test
    @DisplayName("ByteBuffer-like API for slice views")
    public void slice() {
        val buffer = VBufferHandler.newBuffer(LayoutA.class, ByteBuffer::allocateDirect, SIZE_A);

        // Fill buffer
        for (var i = 0; i < SIZE_A; i++) {
            buffer.position(i)
                  .normal(i)
                  .color(i)
                  .texture(i);
            buffer.v$next();
        }

        // Read the slice buffer
        val sliceBuffer = buffer.v$sliceView(3, 7);
        for (var i = 0; i < 7; i++) {
            assertEquals(i + 3, sliceBuffer.position());
            assertEquals(i + 3, sliceBuffer.normal());
            assertEquals(i + 3, sliceBuffer.color());
            assertEquals(i + 3, sliceBuffer.texture());
            sliceBuffer.v$next();
        }
    }

    @Test
    @DisplayName("ByteBuffer-like API for read only views")
    public void readOnly() {
        val buffer = VBufferHandler.newBuffer(LayoutA.class, ByteBuffer::allocateDirect, SIZE_A);

        // Fill buffer
        for (var i = 0; i < SIZE_A; i++) {
            buffer.position(i)
                  .normal(i)
                  .color(i)
                  .texture(i);
            buffer.v$next();
        }
        buffer.v$flip();

        val readOnlyBuffer = buffer.v$asReadOnlyView();

        // Read the read-only buffer
        for (var i = 0; i < SIZE_A; i++) {
            assertEquals(i, readOnlyBuffer.position());
            assertEquals(i, readOnlyBuffer.normal());
            assertEquals(i, readOnlyBuffer.color());
            assertEquals(i, readOnlyBuffer.texture());
            readOnlyBuffer.v$next();
        }
        readOnlyBuffer.v$flip();

        // Ensure correct exceptions thrown
        assertThrows(ReadOnlyBufferException.class, () -> readOnlyBuffer.position(0)
                                                                        .normal(0)
                                                                        .color(0)
                                                                        .texture(0));
        assertThrows(ReadOnlyBufferException.class, readOnlyBuffer::v$compact);
    }

    @Test
    @DisplayName("Iterators")
    public void iteration() {
        val buffer = VBufferHandler.newBuffer(LayoutA.class, ByteBuffer::allocateDirect, SIZE_A);

        // Fill buffer
        var value = 0;
        for (val stride : buffer) {
            stride.position(value)
                  .normal(value)
                  .color(value)
                  .texture(value);
            value++;
        }

        // Read buffer
        value = 0;
        for (val stride : buffer) {
            assertEquals(value, stride.position());
            assertEquals(value, stride.normal());
            assertEquals(value, stride.color());
            assertEquals(value, stride.texture());
            value++;
        }
    }

    @Test
    @DisplayName("Streams")
    public void streams() {
        val buffer = VBufferHandler.newBuffer(LayoutA.class, ByteBuffer::allocateDirect, SIZE_D);

        // Create immutable list of integers
        val testValues = IntStream.range(0, SIZE_D).boxed().toList();

        val tempList = new ArrayList<>(testValues);
        buffer.v$stream().forEach(layoutB -> {
            // Get the first element from the list
            val value = tempList.remove(0);
            // Write the value to the buffer
            layoutB.position(value)
                   .normal(value)
                   .color(value)
                   .texture(value);
        });

        tempList.addAll(testValues);
        buffer.v$stream().forEach(layoutB -> {
            // Get the first element from the list
            val value = tempList.remove(0);
            // Read the value from the buffer
            assertEquals(value, layoutB.position());
            assertEquals(value, layoutB.normal());
            assertEquals(value, layoutB.color());
            assertEquals(value, layoutB.texture());
        });
    }

    @Test
    @DisplayName("Parallel Streams")
    public void parallelStreams() {
        val buffer = VBufferHandler.newBuffer(LayoutA.class, ByteBuffer::allocateDirect, SIZE_D);
        val testValues = IntStream.range(0, SIZE_D).boxed().toList();

        // Fill buffer in parallel
        val tempList = Collections.synchronizedList(new ArrayList<>(testValues));
        buffer.v$parallelStream().forEach(layoutB -> {
            // Get the first element from the list
            val value = tempList.remove(0);
            // Write the value to the buffer
            layoutB.position(value)
                   .normal(value)
                   .color(value)
                   .texture(value);
        });

        // Read buffer in parallel
        buffer.v$parallelStream().forEach(layoutB -> {
            val value = layoutB.position();
            assertEquals(value, layoutB.normal());
            assertEquals(value, layoutB.color());
            assertEquals(value, layoutB.texture());
            tempList.add(value);
        });

        // Ensure all values were read
        tempList.sort(Integer::compareTo);
        assertEquals(testValues, tempList);
    }

    @Test
    @DisplayName("Internal stride copying")
    public void internalCopy() {
        val buffer = VBufferHandler.newBuffer(LayoutB.class, ByteBuffer::allocateDirect, SIZE_A);

        val position = new Vector3f(55F, 994F, -1515F);
        val normal = new Vector3f(35F, 300F, -105F);
        val color = new Vector4f(7777F, 0F, -1F, 1000F);
        val texture = new Vector2f(-642F, 0.66F);

        // Write the test data
        buffer.v$position(3);
        buffer.position(position)
              .normal(normal)
              .color(color)
              .texture(texture);

        // Copy the test data
        buffer.v$copyStride(9, 3);

        // Read the test data
        buffer.v$position(9);
        assertEquals(position, buffer.position());
        assertEquals(normal, buffer.normal());
        assertEquals(color, buffer.color());
        assertEquals(texture, buffer.texture());
    }

    @Test
    @DisplayName("ByteBuffer-like API for read only views")
    public void bufferToBufferCopy() {
        val bufferA = VBufferHandler.newBuffer(LayoutA.class, ByteBuffer::allocateDirect, SIZE_A);
        val bufferB = VBufferHandler.newBuffer(LayoutA.class, ByteBuffer::allocateDirect, SIZE_A);

        // Fill buffer A
        for (var i = 0; i < SIZE_A; i++) {
            bufferA.position(i)
                   .normal(i)
                   .color(i)
                   .texture(i);
            bufferA.v$next();
        }
        bufferA.v$flip();

        // Copy buffer A to buffer B
        bufferB.v$put(bufferA);
        bufferA.v$flip();
        bufferB.v$flip();

        // Read buffer B
        for (var i = 0; i < SIZE_A; i++) {
            assertEquals(i, bufferB.position());
            assertEquals(i, bufferB.normal());
            assertEquals(i, bufferB.color());
            assertEquals(i, bufferB.texture());
            bufferB.v$next();
        }
        bufferB.v$flip();
    }
}