package com.github.basdxz.vbuffers;

import com.github.basdxz.vbuffers.internal.feature.BufferHandler;
import com.github.basdxz.vbuffers.samples.LayoutA;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class Benchmark {
    protected static final int BUFFER_SIZE_C = 1000;

//    @Test
    public void streams() {
        var startTime = System.nanoTime();
        var buffer = demoNewBuffer();
        var endTime = System.nanoTime();

        // Print microseconds to create the buffer
        System.out.println("Allocation Pre JIT: " + (endTime - startTime) / 1000 + "us");

        for (int i = 0; i < 1000; i++) {
            buffer = demoNewBuffer();
        }

        startTime = System.nanoTime();
        buffer = demoNewBuffer();
        endTime = System.nanoTime();
        // Print microseconds to create the buffer
        System.out.println("Allocation Post JIT: " + (endTime - startTime) / 1000 + "us");

        // Create a list of integers from 0 to 9
        val testValues = IntStream.range(0, BUFFER_SIZE_C).boxed().toList();

        startTime = System.nanoTime();
        val tempList = new ArrayList<>(testValues);
        buffer.v$stream().forEach(demoWriter(tempList));
        tempList.addAll(testValues);
        buffer.v$stream().forEach(demoReader(tempList));
        endTime = System.nanoTime();
        System.out.println("IO Pre JIT: " + (endTime - startTime) / 1000 + "us");

        // Warm up the JIT
        for (var i = 0; i < 1000; i++) {
            tempList.addAll(testValues);
            buffer.v$stream().forEach(demoWriter(tempList));
            tempList.addAll(testValues);
            buffer.v$stream().forEach(demoReader(tempList));
        }

        startTime = System.nanoTime();
        tempList.addAll(testValues);
        buffer.v$stream().forEach(demoWriter(tempList));
        tempList.addAll(testValues);
        buffer.v$stream().forEach(demoReader(tempList));
        endTime = System.nanoTime();
        System.out.println("IO Post JIT: " + (endTime - startTime) / 1000 + "us");
    }

    private static LayoutA demoNewBuffer() {
        return BufferHandler.newBuffer(LayoutA.class, ByteBuffer::allocateDirect, BUFFER_SIZE_C);
    }

    @NotNull
    private static Consumer<LayoutA> demoWriter(ArrayList<Integer> tempList) {
        return layoutB -> {
            // Get the first element from the list
            val value = tempList.remove(0);
            // Write the value to the buffer
            layoutB.position(value)
                   .normal(value)
                   .color(value)
                   .texture(value);
        };
    }

    @NotNull
    private static Consumer<LayoutA> demoReader(ArrayList<Integer> tempList) {
        return layoutB -> {
            // Get the first element from the list
            val value = tempList.remove(0);
            // Read the value from the buffer
            assertEquals(value, layoutB.position());
            assertEquals(value, layoutB.normal());
            assertEquals(value, layoutB.color());
            assertEquals(value, layoutB.texture());
        };
    }

    //    @Test
    public void byteBufferPerformanceTest() {
        // Create bytebuffer to fit 4000 ints
        val buffer = ByteBuffer.allocateDirect(4000 * Integer.BYTES);
        // Fill buffer with 4000 ints
        for (var i = 0; i < 4000; i++) {
            buffer.putInt(i);
        }
        // Flip the buffer
        buffer.flip();
        // Read the buffer till the end
        for (var i = 0; i < 4000; i++) {
            assertEquals(i, buffer.getInt());
        }
    }
}
