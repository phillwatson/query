package com.hillayes.query.filter.expression;

public class Expression {
    private Argument leftArg;
    private Operator operator;
    private Argument rightArg;

    public Expression setLeftArg(Argument leftArg) {
        this.leftArg = leftArg;
        return this;
    }

    public <T extends Argument> T getLeftArg() {
        return (T)leftArg;
    }

    public Expression setOperator(Operator operator) {
        this.operator = operator;
        return this;
    }

    public Operator getOperator() {
        return operator;
    }

    public Expression setRightArg(Argument rightArg) {
        this.rightArg = rightArg;
        return this;
    }

    public <T extends Argument> T getRightArg() {
        return (T)rightArg;
    }

    public String toString(Argument.Visitor<String> visitor) {
        StringBuilder result = new StringBuilder();
        if (leftArg != null) {
            result.append(leftArg.accept(visitor));
        }
        if (operator != null) {
            result.append(" ").append(operator).append(" ");
        }
        if (rightArg != null) {
            result.append(rightArg.accept(visitor));
        }
        return result.toString();
    }
}
