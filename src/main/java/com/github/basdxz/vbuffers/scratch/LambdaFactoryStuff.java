package com.github.basdxz.vbuffers.scratch;

import lombok.*;

import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.Arrays;

@SuppressWarnings("unchecked")
public class LambdaFactoryStuff {

    public static class RoutingContext {
        // ...
    }

    @FunctionalInterface
    public interface Handler<X> {
        public void handle(X arg);
    }

    public static class HomeHandler implements Handler<RoutingContext> {
        @Override
        public void handle(RoutingContext ctx) {
            // ...

        }

        public static void bamboozle(RoutingContext ctx) {
            System.out.println("foo");
            System.out.println("foo");
            System.out.println("foo");
            System.out.println("foo");
            System.out.println("foo");
        }
    }

    public static void main(String[] args) throws Throwable {
        newLambdaFactory(Handler.class, HomeHandler.class.getDeclaredMethod("bamboozle", RoutingContext.class));
    }

    public static MethodHandle newLambdaFactory(Class<?> functionalInterface, Method staticMethod) {
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
            val interfaceMethodType = MethodType.methodType(interfaceMethod.getReturnType(), interfaceMethod.getParameterTypes());

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
            throw new RuntimeException("Failed to bind method: %s to functional interface: %s"
                                               .formatted(staticMethod.getName(), functionalInterface.getName()),
                                       throwable);
        }
    }
}
