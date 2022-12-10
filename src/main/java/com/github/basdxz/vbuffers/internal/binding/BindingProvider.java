package com.github.basdxz.vbuffers.internal.binding;

import com.github.basdxz.vbuffers.binding.Bindings;
import com.github.basdxz.vbuffers.binding.GetterBinding;
import com.github.basdxz.vbuffers.binding.SetterBinding;
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

import static com.github.basdxz.vbuffers.binding.Bindings.*;

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
        val annotation = method.getAnnotation(Put.class);
        if (annotation == null)
            return;
        val classTypes = annotation.value();
        if (classTypes.length == 0)
            throw new IllegalArgumentException("Setter method " + method.getName() + " has no class types");
        val lambda = newLambdaFactory(SetterBinding.class, method).invoke();
        for (val classType : classTypes)
            setters.put(classType, (SetterBinding<?>) lambda);
    }

    public static void addGetterIfAnnotated(Method method) throws Throwable {
        val annotation = method.getAnnotation(Get.class);
        if (annotation == null)
            return;
        val classTypes = annotation.value();
        if (classTypes.length == 0)
            throw new IllegalArgumentException("Getter method " + method.getName() + " has no class types");
        val lambda = newLambdaFactory(GetterBinding.class, method).invoke();
        for (val classType : classTypes)
            getters.put(classType, (GetterBinding<?>) lambda);
    }

    public static void addImutableGetterIfAnnotated(Method method) throws Throwable {
        val annotation = method.getAnnotation(ImGet.class);
        if (annotation == null)
            return;
        val classTypes = annotation.value();
        if (classTypes.length == 0)
            throw new IllegalArgumentException("Getter method " + method.getName() + " has no class types");
        val lambda = newLambdaFactory(GetterBinding.Immutable.class, method).invoke();
        for (val classType : classTypes)
            getters.put(classType, (GetterBinding.Immutable<?>) lambda);
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