package com.github.basdxz.vbuffers.internal.accessor;

import com.github.basdxz.vbuffers.accessor.Accessor;
import lombok.*;

import java.nio.ByteBuffer;
import java.util.List;

final class AccessorHandler implements Accessor {
    private final IdxBinding idxBinding;
    private final AccessorReturn accessorReturn;
    private final List<AccessorParameter> accessorParameters;
    @Getter
    private final boolean writing;

    AccessorHandler(IdxBinding idxBinding, AccessorReturn accessorReturn, List<AccessorParameter> accessorParameters) {
        this.idxBinding = idxBinding;
        this.accessorReturn = accessorReturn;
        this.accessorParameters = accessorParameters;

        //Check if any of the parameter handlers have matching attributes
        for (val parameterHandler : accessorParameters) {
            val attribute = parameterHandler.attribute();
            val matchingParameterHandler = accessorParameters.stream()
                                                             .filter(handler -> handler != parameterHandler)
                                                             .filter(handler -> attribute.equals(handler.attribute()))
                                                             .findFirst();
            if (matchingParameterHandler.isPresent())
                throw new IllegalArgumentException("Multiple parameter handlers with the same attribute " + attribute.name());
        }
        this.writing = accessorParameters.stream().anyMatch(accessorParameter -> accessorParameter instanceof InParameter);
    }

    @Override
    public Object access(Object chainable, ByteBuffer back, int offsetBytes, Object... args) {
        offsetBytes += strideOffsetBytes(args);
        for (val handler : accessorParameters)
            handler.handle(back, offsetBytes, args);
        return accessorReturn.handle(chainable, back, offsetBytes, args);
    }

    protected int strideOffsetBytes(Object... args) {
        if (idxBinding == null)
            return 0;
        return idxBinding.strideOffset(args);
    }
}
