package com.hillayes.query.predicate;

import com.hillayes.query.filter.Operator;
import com.hillayes.query.filter.QueryPredicate;
import org.junit.jupiter.api.Test;

public class PredicateTest {
    @Test
    public void test1() {
        QueryPredicate propertyPredicate =
            new PropertyPredicate("3", Operator.EQ, 3)
                .or(
                    new PropertyPredicate("2", Operator.NE, 2)
                    .and(new PropertyPredicate("4", Operator.GE, 4))
                );

        System.out.println(propertyPredicate);
    }

    @Test
    public void test2() {
        QueryPredicate propertyPredicate =
            new PropertyPredicate("3.00F", Operator.EQ, 3.00F)
                .or(new PropertyPredicate("2L", Operator.NE, 2L))
                .and(new PropertyPredicate("4", Operator.GE, 4));

        System.out.println(propertyPredicate);
    }

    @Test
    public void test3() {
        QueryPredicate propertyPredicate =
            new PropertyPredicate("3", Operator.EQ, 3)
                .and(
                    new PropertyPredicate("2", Operator.NE, 2)
                        .or(new PropertyPredicate("4", Operator.GE, 4))
                );

        System.out.println(propertyPredicate);
    }
}
