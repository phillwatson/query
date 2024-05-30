package com.hillayes.query.filter.expression;

import com.hillayes.query.filter.function.Function;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FunctionArgument implements Argument {
    private final Function function;
    private final List<Argument> arguments = new ArrayList<>(2);

    public FunctionArgument(Function aFunction) {
        function = aFunction;
    }

    public Function getFunction() {
        return function;
    }

    public List<Argument> getArguments() {
        return arguments;
    }

    public void addArgument(Argument aArgument) {
        arguments.add(aArgument);
    }

    public String toString() {
        String args = arguments.stream().map(Object::toString).collect(Collectors.joining(", "));
        return function + "(" + args + ")";
    }

    public <R> R accept(Visitor<R> aVisitor) {
        return aVisitor.visit(this);
    }
}
