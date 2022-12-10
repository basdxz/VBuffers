package com.github.basdxz.vbuffers.accessor.front.impl;

import com.github.basdxz.vbuffers.accessor.front.FrontAccessor;
import com.github.basdxz.vbuffers.accessor.front.bind.ParameterBinding;
import com.github.basdxz.vbuffers.accessor.front.bind.ReturnBinding;
import com.github.basdxz.vbuffers.accessor.front.bind.impl.IdxBinding;
import com.github.basdxz.vbuffers.accessor.front.bind.impl.InParameterBinding;
import lombok.*;

import java.nio.ByteBuffer;
import java.util.List;

public class FrontAccessorHandler implements FrontAccessor {
    protected final IdxBinding idxBinding;
    protected final ReturnBinding returnBinding;
    protected final List<ParameterBinding> parameterBindings;
    @Getter
    protected final boolean writing;

    public FrontAccessorHandler(IdxBinding idxBinding, ReturnBinding returnBinding, List<ParameterBinding> parameterBindings) {
        this.idxBinding = idxBinding;
        this.returnBinding = returnBinding;
        this.parameterBindings = parameterBindings;

        //Check if any of the parameter handlers have matching attributes
        for (val parameterHandler : parameterBindings) {
            val attribute = parameterHandler.attribute();
            val matchingParameterHandler = parameterBindings.stream()
                                                            .filter(handler -> handler != parameterHandler)
                                                            .filter(handler -> attribute.equals(handler.attribute()))
                                                            .findFirst();
            if (matchingParameterHandler.isPresent())
                throw new IllegalArgumentException("Multiple parameter handlers with the same attribute " + attribute.name());
        }
        this.writing = parameterBindings.stream().anyMatch(parameterBinding -> parameterBinding instanceof InParameterBinding);
    }

    @Override
    public Object access(Object chainable, ByteBuffer back, int offsetBytes, Object... args) {
        offsetBytes += strideOffsetBytes(args);
        for (val handler : parameterBindings)
            handler.handle(back, offsetBytes, args);
        return returnBinding.handle(chainable, back, offsetBytes, args);
    }

    protected int strideOffsetBytes(Object... args) {
        if (idxBinding == null)
            return 0;
        return idxBinding.strideOffset(args);
    }
}
