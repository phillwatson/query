package com.hillayes.query.filter.expression;

public class NumericArgument implements Argument {
    private final Number value;

    public NumericArgument(String aValue) {
        if (aValue.contains("."))
            value = Double.parseDouble(aValue);
        else
            value = Long.parseLong(aValue);
    }

    public NumericArgument(Number aValue) {
        value = aValue;
    }

    public Number getValue() {
        return value;
    }

    public String toString() {
        return value.toString();
    }

    public <R> R accept(Visitor<R> aVisitor) {
        return aVisitor.visit(this);
    }
}
