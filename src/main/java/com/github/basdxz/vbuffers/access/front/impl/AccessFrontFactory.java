package com.github.basdxz.vbuffers.access.front.impl;

import com.github.basdxz.vbuffers.access.front.AccessFront;
import com.github.basdxz.vbuffers.access.front.ParameterHandler;
import com.github.basdxz.vbuffers.access.front.ReturnHandler;
import com.github.basdxz.vbuffers.layout.Stride;
import lombok.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AccessFrontFactory {
    public static Map<Method, AccessFront> accessFronts(Object chainable, Stride stride, Class<?> layout) {
        val accessFronts = new HashMap<Method, AccessFront>();
        for (val method : layout.getMethods()) {
            val accessFront = create(chainable, stride, method);
            accessFronts.put(method, accessFront);
        }
        return accessFronts;
    }

    public static AccessFront create(Object chainable, Stride stride, Method method) {
        val idxHandler = newIdxHandler(stride, method);
        val parameterHandlers = newParameterHandlers(stride, method);
        val returnHandler = newReturnHandler(chainable, stride, method, parameterHandlers);
        return new SimpleFront(idxHandler, returnHandler, parameterHandlers);
    }

    private static IdxHandler newIdxHandler(Stride stride, Method method) {
        val parameters = method.getParameters();
        for (var i = 0; i < parameters.length; i++) {
            val annotations = parameters[i].getAnnotatedType().getAnnotations();
            for (val annotation : annotations)
                if (annotation instanceof AccessFront.Idx)
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
                if (annotation instanceof AccessFront.In inAnnotation)
                    parameterHandlers.add(new InParameterHandler(stride, inAnnotation, i));
                if (annotation instanceof AccessFront.Out outAnnotation)
                    parameterHandlers.add(new OutParameterHandler(stride, outAnnotation, i));
            }
        }
        return parameterHandlers;
    }

    private static ReturnHandler newReturnHandler(Object chainable,
                                                  Stride stride,
                                                  Method method,
                                                  List<ParameterHandler> parameterHandlers) {
        val annotations = method.getAnnotatedReturnType().getAnnotations();
        for (val annotation : annotations) {
            if (annotation instanceof AccessFront.Chain)
                return new ChainReturnHandler(chainable);
            if (annotation instanceof AccessFront.In in) {
                val inParameter = parameterHandlers.stream()
                                                   .filter(parameterHandler -> in.value().equals(parameterHandler.attribute().name()))
                                                   .filter(parameterHandler -> parameterHandler instanceof InParameterHandler)
                                                   .map(parameterHandler -> (InParameterHandler) parameterHandler)
                                                   .findFirst();
                if (inParameter.isEmpty())
                    throw new IllegalArgumentException("No in parameter with name " + in.value() + " found");
                return new InReturnHandler(inParameter.get());
            }
            if (annotation instanceof AccessFront.Out out) {
                val outParameter = parameterHandlers.stream()
                                                    .filter(parameterHandler -> out.value().equals(parameterHandler.attribute().name()))
                                                    .filter(parameterHandler -> parameterHandler instanceof OutParameterHandler)
                                                    .map(parameterHandler -> (OutParameterHandler) parameterHandler)
                                                    .findFirst();
                return new OutReturnHandler(stride, (AccessFront.Out) annotation, outParameter.orElse(null));
            }
        }
        return new VoidReturnHandler();
    }
}
