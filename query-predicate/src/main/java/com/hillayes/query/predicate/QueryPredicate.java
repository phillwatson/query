package com.hillayes.query.predicate;

public interface QueryPredicate {
    boolean test();

    default QueryPredicate and(QueryPredicate other) {
        return () -> test() && other.test();
    }

    default QueryPredicate or(QueryPredicate other) {
        return () -> test() || other.test();
    }

    default QueryPredicate not() {
        return () -> !test();
    }
}
