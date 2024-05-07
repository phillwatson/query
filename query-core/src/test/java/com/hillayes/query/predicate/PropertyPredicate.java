package com.hillayes.query.predicate;

import com.hillayes.query.filter.Operator;
import com.hillayes.query.filter.PropertyPath;
import com.hillayes.query.filter.QueryPredicate;

public class PropertyPredicate implements QueryPredicate {
    private final PropertyPath property;
    private final Operator operator;
    private final Comparable<?> value;

    public PropertyPredicate(String path, Operator operator, Comparable<?> value) {
        this.property = PropertyPath.of(path);
        this.operator = operator;
        this.value = value;
    }

    public PropertyPath getProperty() {
        return property;
    }

    public Operator getOperator() {
        return operator;
    }

    public Comparable<?> getValue() {
        return value;
    }

    @Override
    public boolean test() {
        return operator.test(property, value);
    }

    public String toString() {
        return property + " " + operator.getMnemonic() + " " + value;
    }
}
