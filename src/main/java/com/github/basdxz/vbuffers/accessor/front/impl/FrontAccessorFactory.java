package com.github.basdxz.vbuffers.accessor.front.impl;

import com.github.basdxz.vbuffers.accessor.front.FrontAccessor;
import com.github.basdxz.vbuffers.accessor.front.handler.ParameterHandler;
import com.github.basdxz.vbuffers.accessor.front.handler.ReturnHandler;
import com.github.basdxz.vbuffers.accessor.front.handler.impl.*;
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

    private static IdxHandler newIdxHandler(Stride stride, Method method) {
        val parameters = method.getParameters();
        for (var i = 0; i < parameters.length; i++) {
            val annotations = parameters[i].getAnnotatedType().getAnnotations();
            for (val annotation : annotations)
                if (annotation instanceof Layout.Idx)
                    return new IdxHandler(i, stride.sizeBytes());
        }
        return null;
    }

    private static List<ParameterHandler> newParameterHandlers(Stride stride, Method method) {
        val parameterHandlers = new ArrayList<ParameterHandler>();
        val parameters = method.getParameters();
        for (var i = 0; i < parameters.length; i++) {
            val annotations = parameters[i].getAnnotatedType().getAnnotations();
            for (val annotation : annotations) {
                if (annotation instanceof Layout.In inAnnotation)
                    parameterHandlers.add(new InParameterHandler(stride, inAnnotation, i));
                if (annotation instanceof Layout.Out outAnnotation)
                    parameterHandlers.add(new OutParameterHandler(stride, outAnnotation, i));
            }
        }
        return parameterHandlers;
    }

    private static ReturnHandler newReturnHandler(Stride stride,
                                                  Method method,
                                                  List<ParameterHandler> parameterHandlers) {
        val annotations = method.getAnnotatedReturnType().getAnnotations();
        for (val annotation : annotations) {
            if (annotation instanceof Layout.Chain)
                return new ChainReturnHandler();
            if (annotation instanceof Layout.In in) {
                val inParameter = parameterHandlers.stream()
                                                   .filter(parameterHandler -> in.value().equals(parameterHandler.attribute().name()))
                                                   .filter(parameterHandler -> parameterHandler instanceof InParameterHandler)
                                                   .map(parameterHandler -> (InParameterHandler) parameterHandler)
                                                   .findFirst();
                if (inParameter.isEmpty())
                    throw new IllegalArgumentException("No in parameter with name " + in.value() + " found");
                return new InReturnHandler(inParameter.get());
            }
            if (annotation instanceof Layout.Out out) {
                val outParameter = parameterHandlers.stream()
                                                    .filter(parameterHandler -> out.value().equals(parameterHandler.attribute().name()))
                                                    .filter(parameterHandler -> parameterHandler instanceof OutParameterHandler)
                                                    .map(parameterHandler -> (OutParameterHandler) parameterHandler)
                                                    .findFirst();
                return new OutReturnHandler(stride, (Layout.Out) annotation, outParameter.orElse(null));
            }
        }
        return new VoidReturnHandler();
    }
}
