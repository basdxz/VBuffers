package com.github.basdxz.vbuffers;

import com.github.basdxz.vbuffers.access.front.impl.AccessFrontFactory;
import com.github.basdxz.vbuffers.layout.Layout;
import com.github.basdxz.vbuffers.layout.impl.BufferStride;
import com.github.basdxz.vbuffers.samples.FrontAccessSimpleSample;
import lombok.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

public class FrontAccessTest {
    @Test
    public void test() throws Throwable {
        val clazz = FrontAccessSimpleSample.class;

        val stride = new BufferStride(clazz.getAnnotation(Layout.class));

        val setterMethod = clazz.getMethod("set", int.class);
        val gettterMethod = clazz.getMethod("get");

        val setterFront = AccessFrontFactory.create(stride, setterMethod);
        val getterFront = AccessFrontFactory.create(stride, gettterMethod);

        val buffer = ByteBuffer.allocate(Integer.BYTES);

        setterFront.access(null, buffer, 0, 5);
        val result = getterFront.access(null, buffer, 0);

        Assertions.assertEquals(5, result);
    }
}
