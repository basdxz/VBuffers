package com.github.basdxz.vbuffers.internal.accessor;

import com.github.basdxz.vbuffers.accessor.Accessor;
import com.github.basdxz.vbuffers.layout.Layout;
import com.github.basdxz.vbuffers.layout.Stride;
import lombok.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AccessorFactory {
    public static Map<Method, Accessor> accessFronts(Stride stride) {
        val accessFronts = new HashMap<Method, Accessor>();
        for (val method : stride.methods()) {// Currently does not account properly for hierarchy
            val accessFront = create(stride, method);
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

    private static AccessorIdxBinding newIdxHandler(Stride stride, Method method) {
        val parameters = method.getParameters();
        for (var i = 0; i < parameters.length; i++) {
            val annotations = parameters[i].getAnnotatedType().getAnnotations();
            for (val annotation : annotations)
                if (annotation instanceof Layout.Idx)
                    return new AccessorIdxBinding(i, stride.sizeBytes());
        }
        return null;
    }

    private static List<AccessorParameter> newParameterHandlers(Stride stride, Method method) {
        val parameterHandlers = new ArrayList<AccessorParameter>();
        val parameters = method.getParameters();
        for (var i = 0; i < parameters.length; i++) {
            var isAnnotated = false;
            val annotations = parameters[i].getAnnotatedType().getAnnotations();
            for (val annotation : annotations) {
                if (annotation instanceof Layout.In inAnnotation) {
                    if (isAnnotated)
                        throw new IllegalArgumentException("Parameter " + i + " of method " + method.getName() + " is annotated multiple times");
                    isAnnotated = true;
                    parameterHandlers.add(new AccessorInParameter(stride, inAnnotation, i));
                }
                if (annotation instanceof Layout.Out outAnnotation) {
                    if (isAnnotated)
                        throw new IllegalArgumentException("Parameter " + i + " of method " + method.getName() + " is annotated multiple times");
                    isAnnotated = true;
                    parameterHandlers.add(new AccessorOutParameter(stride, outAnnotation, i));
                }
            }
            if (!isAnnotated)
                throw new IllegalArgumentException("Parameter " + i + " of method " + method.getName() + " is not annotated");
        }
        return parameterHandlers;
    }

    private static AccessorReturn newReturnHandler(Stride stride,
                                                   Method method,
                                                   List<AccessorParameter> accessorParameters) {
        AccessorReturn accessorReturn = null;
        val annotations = method.getAnnotatedReturnType().getAnnotations();
        for (val annotation : annotations) {
            if (annotation instanceof Layout.Chain) {
                if (accessorReturn != null)
                    throw new IllegalArgumentException("Return of method " + method.getName() + " is annotated multiple times");
                accessorReturn = new AccessorChainReturn();
            }
            if (annotation instanceof Layout.In in) {
                if (accessorReturn != null)
                    throw new IllegalArgumentException("Return of method " + method.getName() + " is annotated multiple times");
                val inParameter = accessorParameters.stream()
                                                    .filter(accessorParameter -> in.value().equals(accessorParameter.attribute().name()))
                                                    .filter(accessorParameter -> accessorParameter instanceof AccessorInParameter)
                                                    .map(accessorParameter -> (AccessorInParameter) accessorParameter)
                                                    .findFirst();
                if (inParameter.isEmpty())
                    throw new IllegalArgumentException("No in parameter with name " + in.value() + " found");
                accessorReturn = new AccessorInReturn(inParameter.get());
            }
            if (annotation instanceof Layout.Out out) {
                if (accessorReturn != null)
                    throw new IllegalArgumentException("Return of method " + method.getName() + " is annotated multiple times");
                val outParameter = accessorParameters.stream()
                                                     .filter(accessorParameter -> out.value().equals(accessorParameter.attribute().name()))
                                                     .filter(accessorParameter -> accessorParameter instanceof AccessorOutParameter)
                                                     .map(accessorParameter -> (AccessorOutParameter) accessorParameter)
                                                     .findFirst();
                accessorReturn = new AccessorOutReturn(stride, (Layout.Out) annotation, outParameter.orElse(null));
            }
        }

        if (accessorReturn == null) {
            if (method.getReturnType() == void.class)
                return new AccessorVoidReturn();
            throw new IllegalArgumentException("Return of method " + method.getName() + " is not annotated");
        }
        return accessorReturn;
    }
}
