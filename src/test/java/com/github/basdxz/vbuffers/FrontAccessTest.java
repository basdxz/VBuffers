package com.github.basdxz.vbuffers;

import com.github.basdxz.vbuffers.access.front.impl.AccessFrontFactory;
import com.github.basdxz.vbuffers.samples.FrontAccessSimpleSample;
import lombok.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

public class FrontAccessTest {
    @Test
    public void test() throws Throwable {
        val buffer = ByteBuffer.allocate(Integer.BYTES);

        val setterMethod = FrontAccessSimpleSample.class.getMethod("set", int.class);
        val gettterMethod = FrontAccessSimpleSample.class.getMethod("get");

        val setterFront = AccessFrontFactory.create(setterMethod);
        val getterFront = AccessFrontFactory.create(gettterMethod);

        setterFront.access(buffer, 0, 5);
        val result = getterFront.access(buffer, 0);
        Assertions.assertEquals(5, result);
    }
}
