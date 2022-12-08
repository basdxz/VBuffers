package com.github.basdxz.vbuffers.accessor.backing.impl;

import com.github.basdxz.vbuffers.accessor.backing.AccessorBackings;
import com.github.basdxz.vbuffers.accessor.backing.GetterBacking;
import com.github.basdxz.vbuffers.accessor.backing.SetterBacking;
import lombok.*;

import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

// TODO: Convert from singleton
public class AccessorBackingProvider {
    protected static final Map<Class<?>, SetterBacking<?>> setters = new HashMap<>();
    protected static final Map<Class<?>, GetterBacking<?>> getters = new HashMap<>();

    static {
        load(PrimitiveBackings.class);
        load(JOMLBackings.class);
    }

    @SuppressWarnings("unchecked")
    public static <T> SetterBacking<T> setter(Class<T> type) {
        return (SetterBacking<T>) setters.get(type);
    }

    @SuppressWarnings("unchecked")
    public static <T> GetterBacking<T> getter(Class<T> type) {
        return (GetterBacking<T>) getters.get(type);
    }

    public static void load(Class<? extends AccessorBackings> accessors) {
        Arrays.stream(accessors.getDeclaredMethods())
              .filter(method -> Modifier.isStatic(method.getModifiers()))
              .forEach(AccessorBackingProvider::addAccessors);
    }

    public static void addAccessors(Method method) {
        try {
            addSetterIfAnnotated(method);
            addGetterIfAnnotated(method);
            addImutableGetterIfAnnotated(method);
        } catch (Throwable t) {
            throw new RuntimeException("Failed to add accessors for method: " + method.getName(), t);
        }
    }

    public static void addSetterIfAnnotated(Method method) throws Throwable {
        val annotation = method.getAnnotation(SetterBacking.Accessor.class);
        if (annotation == null)
            return;
        val classTypes = annotation.value();
        if (classTypes.length == 0)
            throw new IllegalArgumentException("Setter method " + method.getName() + " has no class types");
        val lambda = newLambdaFactory(SetterBacking.class, method).invoke();
        for (val classType : classTypes)
            setters.put(classType, (SetterBacking<?>) lambda);
    }

    public static void addGetterIfAnnotated(Method method) throws Throwable {
        val annotation = method.getAnnotation(GetterBacking.Accessor.class);
        if (annotation == null)
            return;
        val classTypes = annotation.value();
        if (classTypes.length == 0)
            throw new IllegalArgumentException("Getter method " + method.getName() + " has no class types");
        val lambda = newLambdaFactory(GetterBacking.class, method).invoke();
        for (val classType : classTypes)
            getters.put(classType, (GetterBacking<?>) lambda);
    }

    public static void addImutableGetterIfAnnotated(Method method) throws Throwable {
        val annotation = method.getAnnotation(GetterBacking.Immutable.Accessor.class);
        if (annotation == null)
            return;
        val classTypes = annotation.value();
        if (classTypes.length == 0)
            throw new IllegalArgumentException("Getter method " + method.getName() + " has no class types");
        val lambda = newLambdaFactory(GetterBacking.Immutable.class, method).invoke();
        for (val classType : classTypes)
            getters.put(classType, (GetterBacking.Immutable<?>) lambda);
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