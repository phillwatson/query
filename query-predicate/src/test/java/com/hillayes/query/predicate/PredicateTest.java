package com.hillayes.query.predicate;

import com.hillayes.query.filter.expression.Expression;
import com.hillayes.query.filter.expression.NumericArgument;
import com.hillayes.query.filter.expression.Operator;
import org.junit.jupiter.api.Test;

public class PredicateTest {
    @Test
    public void test1() {
        QueryPredicate propertyPredicate =
            new ExpressionPredicate(
                new Expression()
                    .setLeftArg(new NumericArgument("3"))
                    .setOperator(Operator.EQ)
                    .setRightArg(new NumericArgument(3))
            )
                .or(
                    new ExpressionPredicate(
                        new Expression()
                            .setLeftArg(new NumericArgument("2"))
                            .setOperator(Operator.NE)
                            .setRightArg(new NumericArgument(2))
                    )
                        .and(
                            new ExpressionPredicate(
                                new Expression()
                                    .setLeftArg(new NumericArgument("4"))
                                    .setOperator(Operator.GE)
                                    .setRightArg(new NumericArgument(4))
                            )
                        )
                );

        System.out.println(propertyPredicate);
    }

    @Test
    public void test2() {
        QueryPredicate propertyPredicate =
            new ExpressionPredicate(
                new Expression()
                    .setLeftArg(new NumericArgument(3.00F))
                    .setOperator(Operator.EQ)
                    .setRightArg(new NumericArgument(3.00F))
            )
                .or(new ExpressionPredicate(new Expression()
                        .setLeftArg(new NumericArgument(2L))
                        .setOperator(Operator.NE)
                        .setRightArg(new NumericArgument(2L))
                    )
                        .and(new ExpressionPredicate(new Expression()
                            .setLeftArg(new NumericArgument(4))
                            .setOperator(Operator.GE)
                            .setRightArg(new NumericArgument(4))
                        ))
                );

        System.out.println(propertyPredicate);
    }
}
