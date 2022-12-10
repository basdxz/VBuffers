package com.github.basdxz.vbuffers.binding;

import lombok.*;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.github.basdxz.vbuffers.binding.Bindings.*;
import static com.github.basdxz.vbuffers.helper.LambdaHelper.newLambdaMetaFactory;

// TODO: Convert from singleton
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BindingProvider {
    private static final Map<Class<?>, SetterBinding<?>> setters = new HashMap<>();
    private static final Map<Class<?>, GetterBinding<?>> getters = new HashMap<>();

    static {
        load(PrimitiveBindings.class);
        load(JOMLBindings.class);
    }

    @SuppressWarnings("unchecked")
    public static <T> SetterBinding<T> setter(Class<T> type) {
        return (SetterBinding<T>) setters.get(type);
    }

    @SuppressWarnings("unchecked")
    public static <T> GetterBinding<T> getter(Class<T> type) {
        return (GetterBinding<T>) getters.get(type);
    }

    public static void load(Class<? extends Bindings> accessors) {
        Arrays.stream(accessors.getDeclaredMethods())
              .filter(method -> Modifier.isStatic(method.getModifiers()))
              .forEach(BindingProvider::addAccessors);
    }

    private static void addAccessors(Method method) {
        try {
            addSetterIfAnnotated(method);
            addGetterIfAnnotated(method);
            addAllocatingGetterIfAnnotated(method);
        } catch (Throwable t) {
            throw new RuntimeException("Failed to add accessors for method: " + method.getName(), t);
        }
    }

    private static void addSetterIfAnnotated(Method method) throws Throwable {
        val annotation = method.getAnnotation(Put.class);
        if (annotation == null)
            return;
        val classTypes = annotation.value();
        if (classTypes.length == 0)
            throw new IllegalArgumentException("Setter method " + method.getName() + " has no class types");
        val lambda = newLambdaMetaFactory(SetterBinding.class, method).invoke();
        for (val classType : classTypes)
            setters.put(classType, (SetterBinding<?>) lambda);
    }

    private static void addGetterIfAnnotated(Method method) throws Throwable {
        val annotation = method.getAnnotation(Get.class);
        if (annotation == null)
            return;
        val classTypes = annotation.value();
        if (classTypes.length == 0)
            throw new IllegalArgumentException("Getter method " + method.getName() + " has no class types");
        val lambda = newLambdaMetaFactory(GetterBinding.class, method).invoke();
        for (val classType : classTypes)
            getters.put(classType, (GetterBinding<?>) lambda);
    }

    private static void addAllocatingGetterIfAnnotated(Method method) throws Throwable {
        val annotation = method.getAnnotation(NewGet.class);
        if (annotation == null)
            return;
        val classTypes = annotation.value();
        if (classTypes.length == 0)
            throw new IllegalArgumentException("Getter method " + method.getName() + " has no class types");
        val lambda = newLambdaMetaFactory(GetterBinding.Allocating.class, method).invoke();
        for (val classType : classTypes)
            getters.put(classType, (GetterBinding.Allocating<?>) lambda);
    }
}