package com.github.basdxz.vbuffers.access.front.impl;

import com.github.basdxz.vbuffers.access.front.GetterFront;
import com.github.basdxz.vbuffers.access.front.SetterFront;
import lombok.*;

import java.lang.reflect.Method;

public class AccessFrontProvider {
    public static SetterFront setter(Method method) {
        val returnType = method.getReturnType();
        val parameterTypesLength = method.getParameterTypes().length;

        if (returnType == void.class) {
            if (parameterTypesLength == 1)
                System.out.println("Guess: set0");
            if (parameterTypesLength == 2)
                System.out.println("Guess: set1");
        }

        if (returnType.isInterface()) {
            if (parameterTypesLength == 1)
                System.out.println("Guess: set2");
            if (parameterTypesLength == 2)
                System.out.println("Guess: set3");
        }

        if (returnType == int.class) {
            if (parameterTypesLength == 1)
                System.out.println("Guess: set4");
            if (parameterTypesLength == 2)
                System.out.println("Guess: set5");
        }

        return new SetterFront() {
            @Override
            public String toString() {
                return method.getName();
            }
        };
    }

    public static GetterFront getter(Method method) {
        val returnType = method.getReturnType();
        val parameterTypesLength = method.getParameterTypes().length;

        if (returnType == void.class) {
            if (parameterTypesLength == 1)
                System.out.println("Guess: get0");
            if (parameterTypesLength == 2)
                System.out.println("Guess: get1");
        }

        if (returnType.isInterface()) {
            if (parameterTypesLength == 1)
                System.out.println("Guess: get2");
            if (parameterTypesLength == 2)
                System.out.println("Guess: get3");
        }

        if (returnType == int.class) {
            if (parameterTypesLength == 0)
                System.out.println("Guess: get4");
            if (parameterTypesLength == 1)
                System.out.println("Guess: get5/get6");
            if (parameterTypesLength == 2)
                System.out.println("Guess: get7");
        }

        return new GetterFront() {
            @Override
            public boolean isSupported(Method method) {
                return false;
            }

            @Override
            public String toString() {
                return method.getName();
            }
        };
    }
}
