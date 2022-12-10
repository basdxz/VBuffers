package com.github.basdxz.vbuffers.helper;

import lombok.experimental.*;
import lombok.*;

import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.Arrays;

@UtilityClass
public final class LambdaHelper {
    public static MethodHandle newLambdaMetaFactory(Class<?> functionalInterface, Method staticMethod) {
        try {
            // Get the method handle for the functional interface
            if (functionalInterface.getAnnotation(FunctionalInterface.class) == null)
                throw new IllegalArgumentException(functionalInterface.getName() + " is not a functional interface");
            val interfaceMethod = Arrays.stream(functionalInterface.getDeclaredMethods())
                                        .filter(method1 -> !method1.isDefault())
                                        .findFirst()
                                        .orElseThrow();
            val interfaceMethodName = interfaceMethod.getName();
            val factoryType = MethodType.methodType(functionalInterface);
            val interfaceMethodType = MethodType.methodType(interfaceMethod.getReturnType(),
                                                            interfaceMethod.getParameterTypes());

            // Get the method handle for the static method
            val lookup = MethodHandles.lookup();
            val implementation = lookup.unreflect(staticMethod);
            val dynamicMethodType = implementation.type();

            return LambdaMetafactory
                    .metafactory(lookup,
                                 interfaceMethodName,
                                 factoryType,
                                 interfaceMethodType,
                                 implementation,
                                 dynamicMethodType)
                    .getTarget();
        } catch (Throwable throwable) {
            val errStr = "Failed to bind method: %s to functional interface: %s";
            throw new RuntimeException(errStr.formatted(staticMethod.getName(), functionalInterface.getName()),
                                       throwable);
        }
    }
}
