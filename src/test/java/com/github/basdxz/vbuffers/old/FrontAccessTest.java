package com.github.basdxz.vbuffers.old;

import com.github.basdxz.vbuffers.accessor.AccessorFactory;
import com.github.basdxz.vbuffers.layout.Layout;
import com.github.basdxz.vbuffers.layout.Stride;
import com.github.basdxz.vbuffers.old.samples.FrontAccessSimpleSample;
import lombok.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

public class FrontAccessTest {
    @Test
    public void test() throws Throwable {
        val clazz = FrontAccessSimpleSample.class;

        val stride = new Stride(clazz.getAnnotation(Layout.Stride.class));

        val setterMethod = clazz.getMethod("set", int.class);
        val gettterMethod = clazz.getMethod("get");

        val setterFront = AccessorFactory.create(stride, setterMethod);
        val getterFront = AccessorFactory.create(stride, gettterMethod);

        val buffer = ByteBuffer.allocate(Integer.BYTES);

        setterFront.access(null, buffer, 0, 5);
        val result = getterFront.access(null, buffer, 0);

        Assertions.assertEquals(5, result);
    }
}
