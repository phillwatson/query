/**
 * [Phillip Watson] ("COMPANY") CONFIDENTIAL Unpublished Copyright © 2019-2020 Phillip Watson,
 * All Rights Reserved.
 * <p>
 * NOTICE: All information contained herein is, and remains the property of COMPANY. The
 * intellectual and technical concepts contained herein are proprietary to COMPANY and may be
 * covered by U.K. and Foreign Patents, patents in process, and are protected by trade secret or
 * copyright law. Dissemination of this information or reproduction of this material is strictly
 * forbidden unless prior written permission is obtained from COMPANY. Access to the source code
 * contained herein is hereby forbidden to anyone except current COMPANY employees, managers or
 * contractors who have executed Confidentiality and Non-disclosure agreements explicitly covering
 * such access.
 * <p>
 * The copyright notice above does not evidence any actual or intended publication or disclosure of
 * this source code, which includes information that is confidential and/or proprietary, and is a
 * trade secret, of COMPANY. ANY REPRODUCTION, MODIFICATION, DISTRIBUTION, PUBLIC PERFORMANCE, OR
 * PUBLIC DISPLAY OF OR THROUGH USE OF THIS SOURCE CODE WITHOUT THE EXPRESS WRITTEN CONSENT OF
 * COMPANY IS STRICTLY PROHIBITED, AND IN VIOLATION OF APPLICABLE LAWS AND INTERNATIONAL TREATIES.
 * THE RECEIPT OR POSSESSION OF THIS SOURCE CODE AND/OR RELATED INFORMATION DOES NOT CONVEY OR IMPLY
 * ANY RIGHTS TO REPRODUCE, DISCLOSE OR DISTRIBUTE ITS CONTENTS, OR TO MANUFACTURE, USE, OR SELL
 * ANYTHING THAT IT MAY DESCRIBE, IN WHOLE OR IN PART.
 */
package com.hillayes.query.filter;

import com.hillayes.query.filter.expression.*;
import com.hillayes.query.filter.function.BiFunction;
import com.hillayes.query.filter.function.BoolFunction;
import com.hillayes.query.filter.function.UnaryFunction;
import com.hillayes.query.filter.parser.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author <a href="mailto:watson.phill@gmail.com">Phill Watson</a>
 * @since 1.0.0
 */
public class FilterParserTest {
    @Test
    public void testNumericLessThan() throws Exception {
        Node filter = FilterParser.parse("a le 2");
        assertNotNull(filter);

        ExpressionCollector collector = new ExpressionCollector();
        filter.jjtAccept(collector, null);
        List<Expression> expressions = collector.getExpressions();
        assertEquals(1, expressions.size());

        Expression expression = expressions.get(0);
        assertInstanceOf(PropertyArgument.class, expression.getLeftArg());
        assertEquals("a", expression.getLeftArg().toString());
        assertEquals(Operator.LE, expression.getOperator());
        assertInstanceOf(NumericArgument.class, expression.getRightArg());
        assertEquals("2", expression.getRightArg().toString());
    }

    @Test
    public void testOperatorCaseInsensitive() throws Exception {
        Node filter = FilterParser.parse("a LE 2");
        assertNotNull(filter);

        ExpressionCollector collector = new ExpressionCollector();
        filter.jjtAccept(collector, null);
        List<Expression> expressions = collector.getExpressions();

        assertEquals(1, expressions.size());

        Expression expression = expressions.get(0);
        assertInstanceOf(PropertyArgument.class, expression.getLeftArg());
        assertEquals("a", expression.getLeftArg().toString());
        assertEquals(Operator.LE, expression.getOperator());
        assertInstanceOf(NumericArgument.class, expression.getRightArg());
        assertEquals("2", expression.getRightArg().toString());
    }

    @Test
    public void testDoubleQuotes() throws Exception {
        Node filter = FilterParser.parse("property LE \"value\"");
        assertNotNull(filter);

        ExpressionCollector collector = new ExpressionCollector();
        filter.jjtAccept(collector, null);
        List<Expression> expressions = collector.getExpressions();

        assertEquals(1, expressions.size());

        Expression expression = expressions.get(0);
        assertInstanceOf(PropertyArgument.class, expression.getLeftArg());
        assertEquals("property", expression.getLeftArg().toString());
        assertEquals(Operator.LE, expression.getOperator());
        assertInstanceOf(StringArgument.class, expression.getRightArg());
        assertEquals("value", expression.getRightArg().toString());
    }

    @Test
    public void testAnd() throws Exception {
        Node filter = FilterParser.parse("a le 2 and b eq 'xyz'");
        assertNotNull(filter);

        // was the AND operator detected
        assertEquals(1, filter.jjtGetNumChildren());
        Node child = filter.jjtGetChild(0);
        assertInstanceOf(ASTAnd.class, child);

        // where the two comparisons parsed correctly
        ExpressionCollector collector = new ExpressionCollector();
        filter.jjtAccept(collector, null);
        List<Expression> expressions = collector.getExpressions();

        assertEquals(2, expressions.size());

        Expression expression = expressions.get(0);
        assertInstanceOf(PropertyArgument.class, expression.getLeftArg());
        assertEquals("a", expression.getLeftArg().toString());
        assertEquals(Operator.LE, expression.getOperator());
        assertEquals("2", expression.getRightArg().toString());
        assertInstanceOf(NumericArgument.class, expression.getRightArg());

        expression = expressions.get(1);
        assertEquals("b", expression.getLeftArg().toString());
        assertEquals(Operator.EQ, expression.getOperator());
        assertEquals("xyz", expression.getRightArg().toString());
        assertInstanceOf(StringArgument.class, expression.getRightArg());
    }

    @Test
    public void testMultiAnd() throws Exception {
        Node filter = FilterParser.parse("property ne 1 and property ne 2 and property ne 3 and property ne 4");
        assertNotNull(filter);

        // was the AND operator detected
        assertEquals(1, filter.jjtGetNumChildren());
        Node and = filter.jjtGetChild(0);
        assertInstanceOf(ASTAnd.class, and);

        // AND has four comparison nodes
        assertEquals(4, and.jjtGetNumChildren());
        for (int i = 0; i < and.jjtGetNumChildren(); i++) {
            assertInstanceOf(ASTComparison.class, and.jjtGetChild(i));
        }
    }

    @Test
    public void testOr() throws Exception {
        Node filter = FilterParser.parse("a le 2 or b eq 'xyz'");
        assertNotNull(filter);

        // was the OR operator detected
        assertEquals(1, filter.jjtGetNumChildren());
        Node child = filter.jjtGetChild(0);
        assertInstanceOf(ASTOr.class, child);

        // where the two comparisons parsed correctly
        ExpressionCollector collector = new ExpressionCollector();
        filter.jjtAccept(collector, null);
        List<Expression> expressions = collector.getExpressions();

        assertEquals(2, expressions.size());

        Expression expression = expressions.get(0);
        assertEquals("a", expression.getLeftArg().toString());
        assertEquals(Operator.LE, expression.getOperator());
        assertEquals("2", expression.getRightArg().toString());
        assertInstanceOf(NumericArgument.class, expression.getRightArg());

        expression = expressions.get(1);
        assertEquals("b", expression.getLeftArg().toString());
        assertEquals(Operator.EQ, expression.getOperator());
        assertEquals("xyz", expression.getRightArg().toString());
        assertInstanceOf(StringArgument.class, expression.getRightArg());
    }

    @Test
    public void testNot() throws Exception {
        Node filter = FilterParser.parse("not property le 2");
        assertNotNull(filter);

        // was the NOT operator detected
        assertEquals(1, filter.jjtGetNumChildren());
        Node child = filter.jjtGetChild(0);
        assertInstanceOf(ASTNot.class, child);

        // was the comparison parsed correctly
        ExpressionCollector collector = new ExpressionCollector();
        filter.jjtAccept(collector, null);
        List<Expression> expressions = collector.getExpressions();

        assertEquals(1, expressions.size());

        Expression expression = expressions.get(0);
        assertEquals("property", expression.getLeftArg().toString());
        assertEquals(Operator.LE, expression.getOperator());
        assertEquals("2", expression.getRightArg().toString());
        assertInstanceOf(NumericArgument.class, expression.getRightArg());
    }

    @Test
    public void testNotWithAnd() throws Exception {
        Node filter = FilterParser.parse("not property le 2 and property gt 4");
        assertNotNull(filter);

        // was the NOT operator detected
        assertEquals(1, filter.jjtGetNumChildren());
        Node notNode = filter.jjtGetChild(0);
        assertInstanceOf(ASTNot.class, notNode);

        // NOT operator has AND node
        assertEquals(1, notNode.jjtGetNumChildren());
        Node andNode = notNode.jjtGetChild(0);
        assertInstanceOf(ASTAnd.class, andNode);

        // AND node has two comparitors
        assertEquals(2, andNode.jjtGetNumChildren());
        assertInstanceOf(ASTComparison.class, andNode.jjtGetChild(0));
        assertInstanceOf(ASTComparison.class, andNode.jjtGetChild(1));
    }

    @Test
    public void testNotComplex() throws Exception {
        Node filter = FilterParser.parse("not (property le 2 or property gt 4)");
        assertNotNull(filter);

        // was the NOT operator detected
        assertEquals(1, filter.jjtGetNumChildren());
        Node notExpression = filter.jjtGetChild(0);
        assertInstanceOf(ASTNot.class, notExpression);

        // the NOT has a single child - the left-parenthesis
        assertEquals(1, notExpression.jjtGetNumChildren());
        Node lParen = notExpression.jjtGetChild(0);
        assertInstanceOf(ASTLParen.class, lParen);

        // the left-parenthesis has a single child - the OR expression
        Node orExpression = lParen.jjtGetChild(0);

        // the OR expression has two children - comparisons
        assertEquals(2, orExpression.jjtGetNumChildren());

        // where the two comparisons parsed correctly
        ASTComparison lhs = (ASTComparison) orExpression.jjtGetChild(0);
        Expression expression = (Expression) lhs.jjtGetValue();
        assertEquals("property", expression.getLeftArg().toString());
        assertEquals(Operator.LE, expression.getOperator());
        assertEquals("2", expression.getRightArg().toString());
        assertInstanceOf(NumericArgument.class, expression.getRightArg());

        ASTComparison rhs = (ASTComparison) orExpression.jjtGetChild(1);
        expression = (Expression) rhs.jjtGetValue();
        assertEquals("property", expression.getLeftArg().toString());
        assertEquals(Operator.GT, expression.getOperator());
        assertEquals("4", expression.getRightArg().toString());
        assertInstanceOf(NumericArgument.class, expression.getRightArg());
    }

    @Test
    public void testPrecedence() throws Exception {
        // in this expression the AND has precedence - normal logic
        Node filter = FilterParser.parse("property_1 le 12 AND property_2 eq 1 OR property_3 eq 3");
        assertNotNull(filter);

        // tree starts with an OR expression
        assertEquals(1, filter.jjtGetNumChildren());
        assertInstanceOf(ASTOr.class, filter.jjtGetChild(0));

        // the OR expression has two nodes (AND expression and "property_3 le 3")
        assertEquals(2, filter.jjtGetChild(0).jjtGetNumChildren());

        // RHS - property comparison "property_3 eq 3"
        Node comparator = filter.jjtGetChild(0).jjtGetChild(1);
        assertInstanceOf(ASTComparison.class, comparator);
        Expression expression = (Expression) ((ASTComparison) comparator).jjtGetValue();
        assertEquals("property_3", expression.getLeftArg().toString());

        // LHS = the AND expression
        ASTAnd andExpr = (ASTAnd) filter.jjtGetChild(0).jjtGetChild(0);
        assertInstanceOf(ASTAnd.class, andExpr);

        // the AND expression has two nodes ("property_1 le 12" and "property_2 eq 1")
        assertEquals(2, andExpr.jjtGetNumChildren());

        // LHS = property comparison "property_1 le 12"
        comparator = andExpr.jjtGetChild(0);
        assertInstanceOf(ASTComparison.class, comparator);
        expression = (Expression) ((ASTComparison) comparator).jjtGetValue();
        assertEquals("property_1", expression.getLeftArg().toString());

        // RHS - property comparison "property_2 eq 1"
        comparator = andExpr.jjtGetChild(1);
        assertInstanceOf(ASTComparison.class, comparator);
        expression = (Expression) ((ASTComparison) comparator).jjtGetValue();
        assertEquals("property_2", expression.getLeftArg().toString());
    }

    @Test
    public void testBracketsPrecedence() throws Exception {
        // in this expression the OR has precedence due to the brackets
        Node filter = FilterParser.parse("property_1 le 12 AND (property_2 eq 1 OR property_3 eq 3)");
        assertNotNull(filter);

        // tree starts with an AND expression
        assertEquals(1, filter.jjtGetNumChildren());
        Node andExpr = filter.jjtGetChild(0);
        assertInstanceOf(ASTAnd.class, andExpr);

        // the AND expression has two nodes ("property_1 le 12" and OR expression)
        assertEquals(2, andExpr.jjtGetNumChildren());

        // LHS = property comparison "property_1 le 12"
        Node andComparison = andExpr.jjtGetChild(0);
        assertInstanceOf(ASTComparison.class, andComparison);
        Expression expression = (Expression) ((ASTComparison) andComparison).jjtGetValue();
        assertEquals("property_1", expression.getLeftArg().toString());

        // RHS = the OR expression enclosed in brackets
        Node lParen = andExpr.jjtGetChild(1);
        Node orExpr = lParen.jjtGetChild(0);
        assertInstanceOf(ASTOr.class, orExpr);

        // the OR expression has two nodes ("property_2 eq 1" and "property_3 eq 3")
        assertEquals(2, orExpr.jjtGetNumChildren());

        // LHS - property comparison "property_2 eq 1"
        Node comparator = orExpr.jjtGetChild(0);
        assertInstanceOf(ASTComparison.class, comparator);
        expression = (Expression) ((ASTComparison) comparator).jjtGetValue();
        assertEquals("property_2", expression.getLeftArg().toString());

        // RHS - property comparison "property_3 eq 3"
        comparator = orExpr.jjtGetChild(1);
        assertInstanceOf(ASTComparison.class, comparator);
        expression = (Expression) ((ASTComparison) comparator).jjtGetValue();
        assertEquals("property_3", expression.getLeftArg().toString());
    }

    @Test
    public void testNotNull() throws Exception {
        Node filter = FilterParser.parse("notnull(property)");
        assertNotNull(filter);

        ExpressionCollector collector = new ExpressionCollector();
        filter.jjtAccept(collector, null);
        List<Expression> expressions = collector.getExpressions();

        assertEquals(1, expressions.size());
        Expression expression = expressions.get(0);

        assertInstanceOf(FunctionArgument.class, expression.getLeftArg());
        FunctionArgument functionArg = expression.getLeftArg();
        assertEquals(BoolFunction.NOTNULL, functionArg.getFunction());

        assertInstanceOf(PropertyArgument.class, functionArg.getArguments().get(0));
        assertEquals("property", functionArg.getArguments().get(0).toString());
    }

    @Test
    public void testIsNull() throws Exception {
        Node filter = FilterParser.parse("isnull(property)");
        assertNotNull(filter);

        ExpressionCollector collector = new ExpressionCollector();
        filter.jjtAccept(collector, null);
        List<Expression> expressions = collector.getExpressions();

        assertEquals(1, expressions.size());
        Expression expression = expressions.get(0);

        assertInstanceOf(FunctionArgument.class, expression.getLeftArg());
        FunctionArgument functionArg = expression.getLeftArg();
        assertEquals(BoolFunction.ISNULL, functionArg.getFunction());

        assertInstanceOf(PropertyArgument.class, functionArg.getArguments().get(0));
        assertEquals("property", functionArg.getArguments().get(0).toString());
    }

    @Test
    public void testToLower() throws Exception {
        Node filter = FilterParser.parse("lower(property) eq 'abc'");
        assertNotNull(filter);

        ExpressionCollector collector = new ExpressionCollector();
        filter.jjtAccept(collector, null);
        List<Expression> expressions = collector.getExpressions();

        assertEquals(1, expressions.size());

        Expression expression = expressions.get(0);

        assertInstanceOf(FunctionArgument.class, expression.getLeftArg());
        FunctionArgument functionArg = expression.getLeftArg();
        assertEquals(UnaryFunction.LOWER, functionArg.getFunction());

        assertInstanceOf(PropertyArgument.class, functionArg.getArguments().get(0));
        assertEquals("property", functionArg.getArguments().get(0).toString());

        assertEquals(Operator.EQ, expression.getOperator());
        assertEquals("abc", expression.getRightArg().toString());
        assertInstanceOf(StringArgument.class, expression.getRightArg());
    }

    @Test
    public void testToUpper() throws Exception {
        Node filter = FilterParser.parse("upper(property) le 'abc'");
        assertNotNull(filter);

        ExpressionCollector collector = new ExpressionCollector();
        filter.jjtAccept(collector, null);
        List<Expression> expressions = collector.getExpressions();

        assertEquals(1, expressions.size());
        Expression expression = expressions.get(0);

        assertInstanceOf(FunctionArgument.class, expression.getLeftArg());
        FunctionArgument functionArg = expression.getLeftArg();
        assertEquals(UnaryFunction.UPPER, functionArg.getFunction());

        assertInstanceOf(PropertyArgument.class, functionArg.getArguments().get(0));
        assertEquals("property", functionArg.getArguments().get(0).toString());

        assertEquals(Operator.LE, expression.getOperator());
        assertEquals("abc", expression.getRightArg().toString());
        assertInstanceOf(StringArgument.class, expression.getRightArg());
    }

    @Test
    public void testContains() throws Exception {
        Node filter = FilterParser.parse("contains(property, 'abc')");
        assertNotNull(filter);

        ExpressionCollector collector = new ExpressionCollector();
        filter.jjtAccept(collector, null);
        List<Expression> expressions = collector.getExpressions();

        assertEquals(1, expressions.size());
        Expression expression = expressions.get(0);

        assertInstanceOf(FunctionArgument.class, expression.getLeftArg());
        FunctionArgument functionArg = expression.getLeftArg();
        assertEquals(BiFunction.CONTAINS, functionArg.getFunction());

        assertInstanceOf(PropertyArgument.class, functionArg.getArguments().get(0));
        assertEquals("property", functionArg.getArguments().get(0).toString());

        assertInstanceOf(StringArgument.class, functionArg.getArguments().get(1));
        assertEquals("abc", functionArg.getArguments().get(1).toString());
    }

    @Test
    public void testStartsWith() throws Exception {
        Node filter = FilterParser.parse("startswith(property, 'abc')");
        assertNotNull(filter);

        ExpressionCollector collector = new ExpressionCollector();
        filter.jjtAccept(collector, null);
        List<Expression> expressions = collector.getExpressions();

        assertEquals(1, expressions.size());

        Expression expression = expressions.get(0);

        assertInstanceOf(FunctionArgument.class, expression.getLeftArg());
        FunctionArgument functionArg = expression.getLeftArg();
        assertEquals(BiFunction.STARTSWITH, functionArg.getFunction());

        assertInstanceOf(PropertyArgument.class, functionArg.getArguments().get(0));
        assertEquals("property", functionArg.getArguments().get(0).toString());

        assertInstanceOf(StringArgument.class, functionArg.getArguments().get(1));
        assertEquals("abc", functionArg.getArguments().get(1).toString());
    }

    @Test
    public void testEndWith() throws Exception {
        Node filter = FilterParser.parse("endswith(property, 'abc')");
        assertNotNull(filter);

        ExpressionCollector collector = new ExpressionCollector();
        filter.jjtAccept(collector, null);
        List<Expression> expressions = collector.getExpressions();

        assertEquals(1, expressions.size());

        Expression expression = expressions.get(0);

        assertInstanceOf(FunctionArgument.class, expression.getLeftArg());
        FunctionArgument functionArg = expression.getLeftArg();
        assertEquals(BiFunction.ENDSWITH, functionArg.getFunction());

        assertInstanceOf(PropertyArgument.class, functionArg.getArguments().get(0));
        assertEquals("property", functionArg.getArguments().get(0).toString());

        assertInstanceOf(StringArgument.class, functionArg.getArguments().get(1));
        assertEquals("abc", functionArg.getArguments().get(1).toString());
    }

    @Test
    public void testEndWithNumeric() throws Exception {
        assertThrows(ParseException.class, () -> FilterParser.parse("endswith(property, 999)"));
    }

    @Test
    public void testEndWithIdentifier() throws Exception {
        assertThrows(ParseException.class, () -> FilterParser.parse("endswith(property, name)"));
    }

    @Test
    public void testFunctionCaseSensitive() throws Exception {
        assertThrows(ParseException.class, () -> FilterParser.parse("iSNull(property)"));
        assertThrows(ParseException.class, () -> FilterParser.parse("NULL(property)"));
        assertThrows(ParseException.class, () -> FilterParser.parse("EndsWith(property, 'abc')"));
        assertThrows(ParseException.class, () -> FilterParser.parse("StartsWith(property, 'abc')"));
        assertThrows(ParseException.class, () -> FilterParser.parse("CONTAINS(property, 'abc')"));
        assertThrows(ParseException.class, () -> FilterParser.parse("Upper(property) le 'abc')"));
        assertThrows(ParseException.class, () -> FilterParser.parse("LOWER(property) le 'abc')"));
    }

    @Test
    public void testFunctionMissingParam() throws Exception {
        assertThrows(ParseException.class, () -> FilterParser.parse("isnull()"));
        assertThrows(ParseException.class, () -> FilterParser.parse("notnull()"));
    }

    @Test
    public void testFunctionMissingParamOne() throws Exception {
        assertThrows(ParseException.class, () -> FilterParser.parse("endswith(a)"));
    }

    @Test
    public void testFunctionMissingParamTwo() throws Exception {
        assertThrows(ParseException.class, () -> FilterParser.parse("endswith()"));
    }

    @Test
    public void testInvalidUnaryFunction() throws Exception {
        assertThrows(ParseException.class, () -> FilterParser.parse("invalid(property) GT 'abc'"));
    }

    @Test
    public void testInvalidFunction() throws Exception {
        assertThrows(ParseException.class, () -> FilterParser.parse("invalid(property,'abc')"));
    }

    private static class ExpressionCollector extends FilterParserDefaultVisitor {
        List<Expression> expressions = new ArrayList<>();

        public Object visit(ASTComparison node, Object data) {
            expressions.add((Expression) node.jjtGetValue());
            return defaultVisit(node, data);
        }

        public List<Expression> getExpressions() {
            return expressions;
        }
    }
}
