package com.netopyr.reduxfx.patcher.property;

import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyProperty;

import java.lang.invoke.MethodHandle;
import java.util.function.Consumer;

public class PropertyAccessor<TYPE, ACTION> extends AbstractAccessor<TYPE, ACTION, TYPE> {

    PropertyAccessor(MethodHandle propertyGetter, Consumer<ACTION> dispatcher) {
        super(propertyGetter, dispatcher);
    }

    @Override
    protected TYPE fxToV(TYPE value) {
        return value;
    }

    @Override
    protected TYPE vToFX(TYPE value) {
        return value;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void setValue(ReadOnlyProperty<TYPE> property, TYPE value) {
        ((Property)property).setValue(value);
    }
}
