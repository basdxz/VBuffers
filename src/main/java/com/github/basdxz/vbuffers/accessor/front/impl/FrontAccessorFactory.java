package com.github.basdxz.vbuffers.accessor.front.impl;

import com.github.basdxz.vbuffers.accessor.front.FrontAccessor;
import com.github.basdxz.vbuffers.accessor.front.bind.ParameterBinding;
import com.github.basdxz.vbuffers.accessor.front.bind.ReturnBinding;
import com.github.basdxz.vbuffers.accessor.front.bind.impl.*;
import com.github.basdxz.vbuffers.layout.Layout;
import com.github.basdxz.vbuffers.layout.Stride;
import lombok.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FrontAccessorFactory {
    public static Map<Method, FrontAccessor> accessFronts(Stride stride) {
        val accessFronts = new HashMap<Method, FrontAccessor>();
        for (val method : stride.methods()) {// Currently does not account properly for hierarchy
            val accessFront = create(stride, method);
            accessFronts.put(method, accessFront);
        }
        return accessFronts;
    }

    public static FrontAccessor create(Stride stride, Method method) {
        val idxHandler = newIdxHandler(stride, method);
        val parameterHandlers = newParameterHandlers(stride, method);
        val returnHandler = newReturnHandler(stride, method, parameterHandlers);
        return new FrontAccessorHandler(idxHandler, returnHandler, parameterHandlers);
    }

    private static IdxBinding newIdxHandler(Stride stride, Method method) {
        val parameters = method.getParameters();
        for (var i = 0; i < parameters.length; i++) {
            val annotations = parameters[i].getAnnotatedType().getAnnotations();
            for (val annotation : annotations)
                if (annotation instanceof Layout.Idx)
                    return new IdxBinding(i, stride.sizeBytes());
        }
        return null;
    }

    private static List<ParameterBinding> newParameterHandlers(Stride stride, Method method) {
        val parameterHandlers = new ArrayList<ParameterBinding>();
        val parameters = method.getParameters();
        for (var i = 0; i < parameters.length; i++) {
            var isAnnotated = false;
            val annotations = parameters[i].getAnnotatedType().getAnnotations();
            for (val annotation : annotations) {
                if (annotation instanceof Layout.In inAnnotation) {
                    if (isAnnotated)
                        throw new IllegalArgumentException("Parameter " + i + " of method " + method.getName() + " is annotated multiple times");
                    isAnnotated = true;
                    parameterHandlers.add(new InParameterBinding(stride, inAnnotation, i));
                }
                if (annotation instanceof Layout.Out outAnnotation) {
                    if (isAnnotated)
                        throw new IllegalArgumentException("Parameter " + i + " of method " + method.getName() + " is annotated multiple times");
                    isAnnotated = true;
                    parameterHandlers.add(new OutParameterBinding(stride, outAnnotation, i));
                }
            }
            if (!isAnnotated)
                throw new IllegalArgumentException("Parameter " + i + " of method " + method.getName() + " is not annotated");
        }
        return parameterHandlers;
    }

    private static ReturnBinding newReturnHandler(Stride stride,
                                                  Method method,
                                                  List<ParameterBinding> parameterBindings) {
        ReturnBinding returnBinding = null;
        val annotations = method.getAnnotatedReturnType().getAnnotations();
        for (val annotation : annotations) {
            if (annotation instanceof Layout.Chain) {
                if (returnBinding != null)
                    throw new IllegalArgumentException("Return of method " + method.getName() + " is annotated multiple times");
                returnBinding = new ChainReturnBinding();
            }
            if (annotation instanceof Layout.In in) {
                if (returnBinding != null)
                    throw new IllegalArgumentException("Return of method " + method.getName() + " is annotated multiple times");
                val inParameter = parameterBindings.stream()
                                                   .filter(parameterBinding -> in.value().equals(parameterBinding.attribute().name()))
                                                   .filter(parameterBinding -> parameterBinding instanceof InParameterBinding)
                                                   .map(parameterBinding -> (InParameterBinding) parameterBinding)
                                                   .findFirst();
                if (inParameter.isEmpty())
                    throw new IllegalArgumentException("No in parameter with name " + in.value() + " found");
                returnBinding = new InReturnBinding(inParameter.get());
            }
            if (annotation instanceof Layout.Out out) {
                if (returnBinding != null)
                    throw new IllegalArgumentException("Return of method " + method.getName() + " is annotated multiple times");
                val outParameter = parameterBindings.stream()
                                                    .filter(parameterBinding -> out.value().equals(parameterBinding.attribute().name()))
                                                    .filter(parameterBinding -> parameterBinding instanceof OutParameterBinding)
                                                    .map(parameterBinding -> (OutParameterBinding) parameterBinding)
                                                    .findFirst();
                returnBinding = new OutReturnBinding(stride, (Layout.Out) annotation, outParameter.orElse(null));
            }
        }

        if (returnBinding == null) {
            if (method.getReturnType() == void.class)
                return new VoidReturnBinding();
            throw new IllegalArgumentException("Return of method " + method.getName() + " is not annotated");
        }
        return returnBinding;
    }
}
