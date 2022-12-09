package com.github.basdxz.vbuffers.access.front.impl;

import com.github.basdxz.vbuffers.access.front.AccessFront;
import lombok.*;

import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class AccessFrontFactory {
    public static AccessFront create(Method method) {
        val returnHandler = newReturnHandler(method);
        val parameterHandlers = newParameterHandlers(method);
        return new SimpleFront(returnHandler, parameterHandlers);
    }

    private static ReturnHandler newReturnHandler(Method method) {
        for (val annotation : method.getAnnotatedReturnType().getAnnotations()) {
            if (annotation instanceof AccessFront.Chain)
                return new ChainReturnHandler(null);
            if (annotation instanceof AccessFront.In)
                return new InReturnHandler();
            if (annotation instanceof AccessFront.Out)
                return new OutReturnHandler();
        }
        return new VoidReturnHandler();
    }

    private static List<ParameterHandler> newParameterHandlers(Method method) {
        val parameterHandlers = new ArrayList<ParameterHandler>();
        val parameters = method.getParameters();

        var idxIndex = -1;
        for (var i = 0; i < parameters.length; i++) {
            val annotations = parameters[i].getAnnotatedType().getAnnotations();
            for (val annotation : annotations) {
                if (annotation instanceof AccessFront.Idx) {
                    if (idxIndex != -1)
                        throw new IllegalArgumentException("Only one parameter can be annotated with @Idx");
                    idxIndex = i;
                }
            }
        }

        for (var i = 0; i < parameters.length; i++) {
            val annotations = parameters[i].getAnnotatedType().getAnnotations();
            for (val annotation : annotations) {
                if (annotation instanceof AccessFront.In)
                    parameterHandlers.add(new InParameterHandler(i, idxIndex));
                if (annotation instanceof AccessFront.Out)
                    parameterHandlers.add(new OutParameterHandler(i, idxIndex));
            }
        }

        return parameterHandlers;
    }

    @AllArgsConstructor
    public static class SimpleFront implements AccessFront {
        protected final ReturnHandler returnHandler;
        protected final List<ParameterHandler> parameterHandlers;

        @Override
        public Object access(ByteBuffer back, int offsetBytes, Object... args) throws Throwable {
            for (val handler : parameterHandlers)
                handler.handle(back, offsetBytes, args);
            return returnHandler.handle(back, offsetBytes, args);
        }
    }

    public interface ReturnHandler {
        Object handle(ByteBuffer back, int offsetBytes, Object... args) throws Throwable;
    }

    public static class VoidReturnHandler implements ReturnHandler {
        @Override
        public Object handle(ByteBuffer back, int offsetBytes, Object... args) throws Throwable {
            return null;
        }
    }

    @AllArgsConstructor
    public static class ChainReturnHandler implements ReturnHandler {
        protected final Object thiz;

        @Override
        public Object handle(ByteBuffer back, int offsetBytes, Object... args) throws Throwable {
            return thiz;
        }
    }

    @AllArgsConstructor
    public static class InReturnHandler implements ReturnHandler {
        @Override
        public Object handle(ByteBuffer back, int offsetBytes, Object... args) throws Throwable {
            return null;
        }
    }

    @AllArgsConstructor
    public static class OutReturnHandler implements ReturnHandler {
        @Override
        public Object handle(ByteBuffer back, int offsetBytes, Object... args) {
            return back.getInt(0);
        }
    }

    public interface ParameterHandler {
        Object handle(ByteBuffer back, int offsetBytes, Object... args) throws Throwable;
    }

    @AllArgsConstructor
    public static class IdxParameterHandler implements ParameterHandler {
        protected final int argumentIndex;

        @Override
        public Object handle(ByteBuffer back, int offsetBytes, Object... args) throws Throwable {
            return args[argumentIndex];
        }
    }

    @AllArgsConstructor
    public static class InParameterHandler implements ParameterHandler {
        protected final int argumentIndex;
        protected final int offsetBytesIndex;

        @Override
        public Object handle(ByteBuffer back, int offsetBytes, Object... args) {
            val value = args[argumentIndex];
            val dirtyCast = (int) value;
            back.putInt(0, dirtyCast);
            return null;
        }
    }

    @AllArgsConstructor
    public static class OutParameterHandler implements ParameterHandler {
        protected final int argumentIndex;
        protected final int offsetBytesIndex;

        @Override
        public Object handle(ByteBuffer back, int offsetBytes, Object... args) throws Throwable {
            return null;
        }
    }
}
