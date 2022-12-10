package com.github.basdxz.vbuffers;

import com.github.basdxz.vbuffers.accessor.front.impl.FrontAccessorFactory;
import com.github.basdxz.vbuffers.layout.Layout;
import com.github.basdxz.vbuffers.layout.impl.LayoutStride;
import com.github.basdxz.vbuffers.samples.FrontAccessSimpleSample;
import lombok.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

public class FrontAccessTest {
    @Test
    public void test() throws Throwable {
        val clazz = FrontAccessSimpleSample.class;

        val stride = new LayoutStride(clazz.getAnnotation(Layout.Stride.class));

        val setterMethod = clazz.getMethod("set", int.class);
        val gettterMethod = clazz.getMethod("get");

        val setterFront = FrontAccessorFactory.create(stride, setterMethod);
        val getterFront = FrontAccessorFactory.create(stride, gettterMethod);

        val buffer = ByteBuffer.allocate(Integer.BYTES);

        setterFront.access(null, buffer, 0, 5);
        val result = getterFront.access(null, buffer, 0);

        Assertions.assertEquals(5, result);
    }
}
