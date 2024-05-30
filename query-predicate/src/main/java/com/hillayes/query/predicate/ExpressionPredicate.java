package com.hillayes.query.predicate;

import com.hillayes.query.filter.expression.Expression;

public class ExpressionPredicate implements QueryPredicate {
    private final Expression expression;

    public ExpressionPredicate(Expression expression) {
        this.expression = expression;
    }

    @Override
    public boolean test() {
        return false;
    }

    public String toString() {
        return expression.toString();
    }
}
