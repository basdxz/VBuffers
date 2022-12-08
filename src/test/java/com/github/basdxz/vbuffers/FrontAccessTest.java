package com.github.basdxz.vbuffers;

import com.github.basdxz.vbuffers.access.front.GetterFront;
import com.github.basdxz.vbuffers.access.front.SetterFront;
import com.github.basdxz.vbuffers.access.front.impl.AccessFrontFactory;
import com.github.basdxz.vbuffers.access.front.impl.AccessFrontProvider;
import lombok.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

public class FrontAccessTest {
    @Test
    public void test() {
        // FrontendAccessorExample.class methods as an array list
        val methods = FrontAccessSample.class.getMethods();
        for (val method : methods) {
            System.out.println("--------------------");

            val setterAnnotation = method.getAnnotation(SetterFront.Access.class);
            val getterAnnotation = method.getAnnotation(GetterFront.Access.class);

            if (setterAnnotation != null && getterAnnotation != null)
                throw new IllegalStateException("Method " + method.getName() + " has both setter and getter annotations");
            if (setterAnnotation == null && getterAnnotation == null)
                throw new IllegalStateException("Method " + method.getName() + " has neither setter nor getter annotations");

            if (setterAnnotation != null) {
                val frontend = AccessFrontProvider.setter(method);
                System.out.println("Setter: " + frontend);
            }

            if (getterAnnotation != null) {
                val frontend = AccessFrontProvider.getter(method);
                System.out.println("Getter: " + frontend);
            }
        }
    }

    @Test
    public void goofyAh() throws Throwable {
        val method = FrontAccessSample.class.getMethod("set0", int.class);
        val factory = new AccessFrontFactory();
        Assertions.assertTrue(factory.isSupported(method));
        val front = factory.create(method);

        val buffer = ByteBuffer.allocate(4);
        front.accept(buffer, 0, 5);
        Assertions.assertEquals(5, buffer.getInt(0));
    }
}
