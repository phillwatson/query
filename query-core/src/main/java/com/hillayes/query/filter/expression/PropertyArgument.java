package com.hillayes.query.filter.expression;

import com.hillayes.query.filter.PropertyPath;

public class PropertyArgument implements Argument {
    private final PropertyPath propertyPath;

    public PropertyArgument(String aPropertyPath) {
        propertyPath = PropertyPath.of(aPropertyPath);
    }

    public PropertyPath getPropertyPath() {
        return propertyPath;
    }

    public String toString() {
        return propertyPath.toString();
    }

    public <R> R accept(Visitor<R> aVisitor) {
        return aVisitor.visit(this);
    }
}
