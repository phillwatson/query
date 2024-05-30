package com.hillayes.query.filter.expression;

public class StringArgument implements Argument {
    private final String value;

    public StringArgument(String aValue) {
        String str = aValue.trim();
        char char1 = str.charAt(0);
        value = ((char1 == '\'') || (char1 == '"'))
            ? str.substring(1, str.length() - 1)
            : str;
    }

    public String getValue() {
        return value;
    }

    public String toString() {
        return value;
    }

    public <R> R accept(Visitor<R> aVisitor) {
        return aVisitor.visit(this);
    }
}
