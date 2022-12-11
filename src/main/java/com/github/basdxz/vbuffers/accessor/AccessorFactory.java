package com.github.basdxz.vbuffers.accessor;

import com.github.basdxz.vbuffers.accessor.io.*;
import com.github.basdxz.vbuffers.layout.Layout;
import com.github.basdxz.vbuffers.layout.LayoutInfo;
import com.github.basdxz.vbuffers.layout.Stride;
import lombok.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AccessorFactory {
    public static Map<Method, Accessor> accessFronts(LayoutInfo<?> layoutInfo) {
        val accessFronts = new HashMap<Method, Accessor>();
        for (val method : layoutInfo.methods()) {// Currently does not account properly for hierarchy
            val accessFront = create(layoutInfo.stride(), method);
            accessFronts.put(method, accessFront);
        }
        return accessFronts;
    }

    public static Accessor create(Stride stride, Method method) {
        val idxHandler = newIdxHandler(stride, method);
        val parameterHandlers = newParameterHandlers(stride, method);
        val returnHandler = newReturnHandler(stride, method, parameterHandlers);
        return new AccessorHandler(idxHandler, returnHandler, parameterHandlers);
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

    private static List<Parameter> newParameterHandlers(Stride stride, Method method) {
        val parameterHandlers = new ArrayList<Parameter>();
        val parameters = method.getParameters();
        for (var i = 0; i < parameters.length; i++) {
            var isAnnotated = false;
            val annotations = parameters[i].getAnnotatedType().getAnnotations();
            for (val annotation : annotations) {
                if (annotation instanceof Layout.In inAnnotation) {
                    if (isAnnotated)
                        throw new IllegalArgumentException("Parameter " + i + " of method " + method.getName() + " is annotated multiple times");
                    isAnnotated = true;
                    parameterHandlers.add(new InParameter(stride, inAnnotation, i));
                }
                if (annotation instanceof Layout.Out outAnnotation) {
                    if (isAnnotated)
                        throw new IllegalArgumentException("Parameter " + i + " of method " + method.getName() + " is annotated multiple times");
                    isAnnotated = true;
                    parameterHandlers.add(new OutParameter(stride, outAnnotation, i));
                }
            }
            if (!isAnnotated)
                throw new IllegalArgumentException("Parameter " + i + " of method " + method.getName() + " is not annotated");
        }
        return parameterHandlers;
    }

    private static Return newReturnHandler(Stride stride,
                                           Method method,
                                           List<Parameter> parameters) {
        Return ret = null;
        val annotations = method.getAnnotatedReturnType().getAnnotations();
        for (val annotation : annotations) {
            if (annotation instanceof Layout.This) {
                if (ret != null)
                    throw new IllegalArgumentException("Return of method " + method.getName() + " is annotated multiple times");
                ret = new ThisReturn();
            }
            if (annotation instanceof Layout.In in) {
                if (ret != null)
                    throw new IllegalArgumentException("Return of method " + method.getName() + " is annotated multiple times");
                val inParameter = parameters.stream()
                                            .filter(parameter -> in.value().equals(parameter.attribute().name()))
                                            .filter(parameter -> parameter instanceof InParameter)
                                            .map(parameter -> (InParameter) parameter)
                                            .findFirst();
                if (inParameter.isEmpty())
                    throw new IllegalArgumentException("No in parameter with name " + in.value() + " found");
                ret = new InReturn(inParameter.get());
            }
            if (annotation instanceof Layout.Out out) {
                if (ret != null)
                    throw new IllegalArgumentException("Return of method " + method.getName() + " is annotated multiple times");
                val outParameter = parameters.stream()
                                             .filter(parameter -> out.value().equals(parameter.attribute().name()))
                                             .filter(parameter -> parameter instanceof OutParameter)
                                             .map(parameter -> (OutParameter) parameter)
                                             .findFirst();
                ret = new OutReturn(stride, (Layout.Out) annotation, outParameter.orElse(null));
            }
        }

        if (ret == null) {
            if (method.getReturnType() == void.class)
                return new VoidReturn();
            throw new IllegalArgumentException("Return of method " + method.getName() + " is not annotated");
        }
        return ret;
    }
}
