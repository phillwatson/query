package com.hillayes.query.filter.expression;

public interface Argument {
    <R> R accept(Visitor<R> aVisitor);

    interface Visitor<R> {
        default R visit(NumericArgument aNumericArgument) { return null; }
        default R visit(StringArgument aStringArgument) { return null; }
        default R visit(PropertyArgument aPropertyArgument) { return null; }
        default R visit(FunctionArgument aFunctionArgument) { return null; }
    }
}
