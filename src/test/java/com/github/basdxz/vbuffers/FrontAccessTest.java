package com.github.basdxz.vbuffers;

import com.github.basdxz.vbuffers.access.front.GetterFront;
import com.github.basdxz.vbuffers.access.front.SetterFront;
import com.github.basdxz.vbuffers.access.front.impl.AccessFrontProvider;
import lombok.*;

public class FrontAccessTest {
    //    @Test
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
}
