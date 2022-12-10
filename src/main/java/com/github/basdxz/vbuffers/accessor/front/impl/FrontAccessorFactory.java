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
    public static Map<Method, FrontAccessor> accessFronts(Stride stride, Class<?> layout) {
        val accessFronts = new HashMap<Method, FrontAccessor>();
        for (val method : layout.getMethods()) {
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
            val annotations = parameters[i].getAnnotatedType().getAnnotations();
            for (val annotation : annotations) {
                if (annotation instanceof Layout.In inAnnotation)
                    parameterHandlers.add(new InParameterBinding(stride, inAnnotation, i));
                if (annotation instanceof Layout.Out outAnnotation)
                    parameterHandlers.add(new OutParameterBinding(stride, outAnnotation, i));
            }
        }
        return parameterHandlers;
    }

    private static ReturnBinding newReturnHandler(Stride stride,
                                                  Method method,
                                                  List<ParameterBinding> parameterBindings) {
        val annotations = method.getAnnotatedReturnType().getAnnotations();
        for (val annotation : annotations) {
            if (annotation instanceof Layout.Chain)
                return new ChainReturnBinding();
            if (annotation instanceof Layout.In in) {
                val inParameter = parameterBindings.stream()
                                                   .filter(parameterBinding -> in.value().equals(parameterBinding.attribute().name()))
                                                   .filter(parameterBinding -> parameterBinding instanceof InParameterBinding)
                                                   .map(parameterBinding -> (InParameterBinding) parameterBinding)
                                                   .findFirst();
                if (inParameter.isEmpty())
                    throw new IllegalArgumentException("No in parameter with name " + in.value() + " found");
                return new InReturnBinding(inParameter.get());
            }
            if (annotation instanceof Layout.Out out) {
                val outParameter = parameterBindings.stream()
                                                    .filter(parameterBinding -> out.value().equals(parameterBinding.attribute().name()))
                                                    .filter(parameterBinding -> parameterBinding instanceof OutParameterBinding)
                                                    .map(parameterBinding -> (OutParameterBinding) parameterBinding)
                                                    .findFirst();
                return new OutReturnBinding(stride, (Layout.Out) annotation, outParameter.orElse(null));
            }
        }
        return new VoidReturnBinding();
    }
}
