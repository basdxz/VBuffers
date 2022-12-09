package com.github.basdxz.vbuffers.access.front.impl;

import com.github.basdxz.vbuffers.access.front.AccessFront;
import com.github.basdxz.vbuffers.access.front.ParameterHandler;
import com.github.basdxz.vbuffers.access.front.ReturnHandler;
import lombok.*;

import java.nio.ByteBuffer;
import java.util.List;

public class SimpleFront implements AccessFront {
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
