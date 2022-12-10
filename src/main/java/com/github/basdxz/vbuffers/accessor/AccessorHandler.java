package com.github.basdxz.vbuffers.accessor;

import com.github.basdxz.vbuffers.accessor.io.IdxBinding;
import com.github.basdxz.vbuffers.accessor.io.InParameter;
import com.github.basdxz.vbuffers.accessor.io.Parameter;
import com.github.basdxz.vbuffers.accessor.io.Return;
import lombok.*;

import java.nio.ByteBuffer;
import java.util.List;

final class AccessorHandler implements Accessor {
    private final IdxBinding idxBinding;
    private final Return ret;
    private final List<Parameter> parameters;
    @Getter
    private final boolean writing;

    AccessorHandler(IdxBinding idxBinding, Return ret, List<Parameter> parameters) {
        this.idxBinding = idxBinding;
        this.ret = ret;
        this.parameters = parameters;

        //Check if any of the parameter handlers have matching attributes
        for (val parameterHandler : parameters) {
            val attribute = parameterHandler.attribute();
            val matchingParameterHandler = parameters.stream()
                                                     .filter(handler -> handler != parameterHandler)
                                                     .filter(handler -> attribute.equals(handler.attribute()))
                                                     .findFirst();
            if (matchingParameterHandler.isPresent())
                throw new IllegalArgumentException("Multiple parameter handlers with the same attribute " + attribute.name());
        }
        this.writing = parameters.stream().anyMatch(parameter -> parameter instanceof InParameter);
    }

    @Override
    public Object access(Object chainable, ByteBuffer back, int offsetBytes, Object... args) {
        offsetBytes += strideOffsetBytes(args);
        for (val handler : parameters)
            handler.handle(back, offsetBytes, args);
        return ret.handle(chainable, back, offsetBytes, args);
    }

    protected int strideOffsetBytes(Object... args) {
        if (idxBinding == null)
            return 0;
        return idxBinding.strideOffset(args);
    }
}
