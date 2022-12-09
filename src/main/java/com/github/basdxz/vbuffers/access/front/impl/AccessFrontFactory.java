package com.github.basdxz.vbuffers.access.front.impl;

import com.github.basdxz.vbuffers.access.back.GetterBack;
import com.github.basdxz.vbuffers.access.back.SetterBack;
import com.github.basdxz.vbuffers.access.back.impl.AccessorBacks;
import com.github.basdxz.vbuffers.access.front.AccessFront;
import com.github.basdxz.vbuffers.layout.Attribute;
import com.github.basdxz.vbuffers.layout.Stride;
import lombok.*;

import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class AccessFrontFactory {
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

    public static class SimpleFront implements AccessFront {
        protected final IdxHandler idxHandler;
        protected final ReturnHandler returnHandler;
        protected final List<ParameterHandler> parameterHandlers;

        public SimpleFront(IdxHandler idxHandler, ReturnHandler returnHandler, List<ParameterHandler> parameterHandlers) {
            this.idxHandler = idxHandler;
            this.returnHandler = returnHandler;
            this.parameterHandlers = parameterHandlers;

            //Check if any of the parameter handlers have matching attributes
            for (val parameterHandler : parameterHandlers) {
                val attribute = parameterHandler.attribute();
                val matchingParameterHandler = parameterHandlers.stream()
                                                                .filter(handler -> handler != parameterHandler)
                                                                .filter(handler -> attribute.equals(handler.attribute()))
                                                                .findFirst();
                if (matchingParameterHandler.isPresent())
                    throw new IllegalArgumentException("Multiple parameter handlers with the same attribute " + attribute.name());
            }
        }

        @Override
        public Object access(ByteBuffer back, int offsetBytes, Object... args) throws Throwable {
            offsetBytes += strideOffsetBytes(args);
            for (val handler : parameterHandlers)
                handler.handle(back, offsetBytes, args);
            return returnHandler.handle(back, offsetBytes, args);
        }

        protected int strideOffsetBytes(Object... args) {
            if (idxHandler == null)
                return 0;
            return idxHandler.strideOffset(args);
        }
    }

    @AllArgsConstructor
    public static class IdxHandler {
        protected final int argumentIndex;
        protected final int strideBytes;

        public int strideOffset(Object... args) {
            return (int) args[argumentIndex] * strideBytes;
        }
    }

    public interface ParameterHandler {
        Attribute attribute();

        int parameterIndex();

        void handle(ByteBuffer back, int offsetBytes, Object... args) throws Throwable;
    }

    public static class InParameterHandler implements ParameterHandler {
        @Getter
        protected final int parameterIndex;
        @Getter
        protected final Attribute attribute;
        protected final SetterBack<Object> setter;

        @SuppressWarnings("unchecked")
        public InParameterHandler(Stride stride, AccessFront.In annotation, int parameterIndex) {
            this.parameterIndex = parameterIndex;
            this.attribute = stride.attributeMap().get(annotation.value());
            this.setter = (SetterBack<Object>) AccessorBacks.setter(this.attribute.type());
        }

        @Override
        public void handle(ByteBuffer back, int offsetBytes, Object... args) {
            setter.put(back, offsetBytes + attribute.offsetBytes(), args[parameterIndex]);
        }
    }

    @AllArgsConstructor
    public static class OutParameterHandler implements ParameterHandler {
        @Getter
        protected final int parameterIndex;
        @Getter
        protected final Attribute attribute;
        protected final GetterBack<Object> getter;

        @SuppressWarnings("unchecked")
        public OutParameterHandler(Stride stride, AccessFront.Out annotation, int parameterIndex) {
            this.parameterIndex = parameterIndex;
            this.attribute = stride.attributeMap().get(annotation.value());
            this.getter = (GetterBack<Object>) AccessorBacks.setter(this.attribute.type());
            if (getter instanceof GetterBack.Immutable)
                throw new IllegalArgumentException("Cannot use immutable getter for out parameter");
        }

        @Override
        public void handle(ByteBuffer back, int offsetBytes, Object... args) {
            getter.get(back, offsetBytes + attribute.offsetBytes(), args[parameterIndex]);
        }
    }

    public interface ReturnHandler {
        Object handle(ByteBuffer back, int offsetBytes, Object... args) throws Throwable;
    }

    public static class VoidReturnHandler implements ReturnHandler {
        @Override
        public Object handle(ByteBuffer back, int offsetBytes, Object... args) {
            return null;
        }
    }

    @AllArgsConstructor
    public static class ChainReturnHandler implements ReturnHandler {
        protected final Object chainable;

        @Override
        public Object handle(ByteBuffer back, int offsetBytes, Object... args) {
            return chainable;
        }
    }

    public static class InReturnHandler implements ReturnHandler {
        protected final int outParameterIndex;

        public InReturnHandler(InParameterHandler inParameter) {
            this.outParameterIndex = inParameter.parameterIndex();
        }

        @Override
        public Object handle(ByteBuffer back, int offsetBytes, Object... args) {
            return args[outParameterIndex];
        }
    }

    public static class OutReturnHandler implements ReturnHandler {
        protected final Attribute attribute;
        protected final GetterBack<Object> getterBack;
        protected final int outParameterIndex;

        @SuppressWarnings("unchecked")
        public OutReturnHandler(Stride stride, AccessFront.Out annotation, OutParameterHandler outParameterHandler) {
            val name = annotation.value();
            this.attribute = stride.attributeMap().get(name);
            this.getterBack = (GetterBack<Object>) AccessorBacks.getter(this.attribute.type());
            this.outParameterIndex = outParameterHandler == null ? -1 : outParameterHandler.parameterIndex();
        }

        @Override
        public Object handle(ByteBuffer back, int offsetBytes, Object... args) {
            val outObject = outObject(args);
            return getterBack.get(back, offsetBytes + attribute.offsetBytes(), outObject);
        }

        protected Object outObject(Object... args) {
            if (outParameterIndex == -1)
                return null;
            return args[outParameterIndex];
        }
    }
}
