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

import com.hillayes.query.filter.parser.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author <a href="mailto:watson.phill@gmail.com">Phill Watson</a>
 * @since 1.0.0
 */
public class FilterParserTest {
    private MockQueryContext context;

    @BeforeEach
    public void init() {
        context = new MockQueryContext();
    }

    @AfterEach
    public void close() {
        // System.out.println(context.queryBuilder().toString());
    }

    @Test
    public void testNumericLessThan() throws Exception {
        Node filter = FilterParser.parse(context, "a le 2");
        assertNotNull(filter);

        List<PredicateExpr> comparisons = context.getComparisons();
        assertEquals(1, comparisons.size());

        PredicateExpr comparison = comparisons.get(0);
        assertEquals("a", comparison.getName());
        assertEquals(Operator.LE, comparison.getOperator());
        assertEquals("2", comparison.getValue());
        assertTrue(comparison.isNumeric());
    }

    @Test
    public void testOperatorCaseInsensitive() throws Exception {
        Node filter = FilterParser.parse(context, "a LE 2");
        assertNotNull(filter);

        List<PredicateExpr> comparisons = context.getComparisons();
        assertEquals(1, comparisons.size());

        PredicateExpr comparison = comparisons.get(0);
        assertEquals("a", comparison.getName());
        assertEquals(Operator.LE, comparison.getOperator());
        assertEquals("2", comparison.getValue());
        assertTrue(comparison.isNumeric());
    }

    @Test
    public void testDoubleQuotes() throws Exception {
        Node filter = FilterParser.parse(context, "property LE \"value\"");
        assertNotNull(filter);

        List<PredicateExpr> comparisons = context.getComparisons();
        assertEquals(1, comparisons.size());

        PredicateExpr comparison = comparisons.get(0);
        assertEquals("property", comparison.getName());
        assertEquals(Operator.LE, comparison.getOperator());
        assertEquals("value", comparison.getValue());
        assertFalse(comparison.isNumeric());
    }

    @Test
    public void testAnd() throws Exception {
        Node filter = FilterParser.parse(context, "a le 2 and b eq 'xyz'");
        assertNotNull(filter);

        // was the AND operator detected
        assertEquals(1, filter.jjtGetNumChildren());
        Node child = filter.jjtGetChild(0);
        assertInstanceOf(ASTAnd.class, child);

        // where the two comparisons parsed correctly
        List<PredicateExpr> comparisons = context.getComparisons();
        assertEquals(2, comparisons.size());

        PredicateExpr comparison = comparisons.get(0);
        assertEquals("a", comparison.getName());
        assertEquals(Operator.LE, comparison.getOperator());
        assertEquals("2", comparison.getValue());
        assertTrue(comparison.isNumeric());

        comparison = comparisons.get(1);
        assertEquals("b", comparison.getName());
        assertEquals(Operator.EQ, comparison.getOperator());
        assertEquals("xyz", comparison.getValue());
        assertFalse(comparison.isNumeric());
    }

    @Test
    public void testMultiAnd() throws Exception {
        Node filter = FilterParser.parse(context,
            "property ne 1 and property ne 2 and property ne 3 and property ne 4");
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
        Node filter = FilterParser.parse(context, "a le 2 or b eq 'xyz'");
        assertNotNull(filter);

        // was the OR operator detected
        assertEquals(1, filter.jjtGetNumChildren());
        Node child = filter.jjtGetChild(0);
        assertInstanceOf(ASTOr.class, child);

        // where the two comparisons parsed correctly
        List<PredicateExpr> comparisons = context.getComparisons();
        assertEquals(2, comparisons.size());

        PredicateExpr comparison = comparisons.get(0);
        assertEquals("a", comparison.getName());
        assertEquals(Operator.LE, comparison.getOperator());
        assertEquals("2", comparison.getValue());
        assertTrue(comparison.isNumeric());

        comparison = comparisons.get(1);
        assertEquals("b", comparison.getName());
        assertEquals(Operator.EQ, comparison.getOperator());
        assertEquals("xyz", comparison.getValue());
        assertFalse(comparison.isNumeric());
    }

    @Test
    public void testNot() throws Exception {
        Node filter = FilterParser.parse(context, "not property le 2");
        assertNotNull(filter);

        // was the NOT operator detected
        assertEquals(1, filter.jjtGetNumChildren());
        Node child = filter.jjtGetChild(0);
        assertInstanceOf(ASTNot.class, child);

        // was the comparison parsed correctly
        List<PredicateExpr> comparisons = context.getComparisons();
        assertEquals(1, comparisons.size());

        PredicateExpr comparison = comparisons.get(0);
        assertEquals("property", comparison.getName());
        assertEquals(Operator.LE, comparison.getOperator());
        assertEquals("2", comparison.getValue());
        assertTrue(comparison.isNumeric());
    }

    @Test
    public void testNotWithAnd() throws Exception {
        Node filter = FilterParser.parse(context, "not property le 2 and property gt 4");
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
        Node filter = FilterParser.parse(context, "not (property le 2 or property gt 4)");
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
        PredicateExpr comparison = (PredicateExpr) lhs.jjtGetValue();
        assertEquals("property", comparison.getName());
        assertEquals(Operator.LE, comparison.getOperator());
        assertEquals("2", comparison.getValue());
        assertTrue(comparison.isNumeric());

        ASTComparison rhs = (ASTComparison) orExpression.jjtGetChild(1);
        comparison = (PredicateExpr) rhs.jjtGetValue();
        assertEquals("property", comparison.getName());
        assertEquals(Operator.GT, comparison.getOperator());
        assertEquals("4", comparison.getValue());
        assertTrue(comparison.isNumeric());
    }

    @Test
    public void testPrecedence() throws Exception {
        // in this expression the AND has precedence - normal logic
        Node filter = FilterParser.parse(context, "property_1 le 12 AND property_2 eq 1 OR property_3 eq 3");
        assertNotNull(filter);

        // tree starts with an OR expression
        assertEquals(1, filter.jjtGetNumChildren());
        Node expression = filter.jjtGetChild(0);
        assertInstanceOf(ASTOr.class, expression);

        // the OR expression has two nodes (AND expression and "property_3 le 3")
        assertEquals(2, expression.jjtGetNumChildren());

        // RHS - property comparison "property_3 eq 3"
        Node comparator = expression.jjtGetChild(1);
        assertInstanceOf(ASTComparison.class, comparator);
        PredicateExpr comparison = (PredicateExpr) ((ASTComparison) comparator).jjtGetValue();
        assertEquals("property_3", comparison.getName());

        // LHS = the AND expression
        expression = expression.jjtGetChild(0);
        assertInstanceOf(ASTAnd.class, expression);

        // the AND expression has two nodes ("property_1 le 12" and "property_2 eq 1")
        assertEquals(2, expression.jjtGetNumChildren());

        // LHS = property comparison "property_1 le 12"
        comparator = expression.jjtGetChild(0);
        assertInstanceOf(ASTComparison.class, comparator);
        comparison = (PredicateExpr) ((ASTComparison) comparator).jjtGetValue();
        assertEquals("property_1", comparison.getName());

        // RHS - property comparison "property_2 eq 1"
        comparator = expression.jjtGetChild(1);
        assertInstanceOf(ASTComparison.class, comparator);
        comparison = (PredicateExpr) ((ASTComparison) comparator).jjtGetValue();
        assertEquals("property_2", comparison.getName());
    }

    @Test
    public void testBracketsPrecedence() throws Exception {
        // in this expression the OR has precedence due to the brackets
        Node filter = FilterParser.parse(context, "property_1 le 12 AND (property_2 eq 1 OR property_3 eq 3)");
        assertNotNull(filter);

        // tree starts with an AND expression
        assertEquals(1, filter.jjtGetNumChildren());
        Node expression = filter.jjtGetChild(0);
        assertInstanceOf(ASTAnd.class, expression);

        // the AND expression has two nodes ("property_1 le 12" and OR expression)
        assertEquals(2, expression.jjtGetNumChildren());

        // LHS = property comparison "property_1 le 12"
        Node andComparison = expression.jjtGetChild(0);
        assertInstanceOf(ASTComparison.class, andComparison);
        PredicateExpr comparison = (PredicateExpr) ((ASTComparison) andComparison).jjtGetValue();
        assertEquals("property_1", comparison.getName());

        // RHS = the OR expression enclosed in brackets
        Node lParen = expression.jjtGetChild(1);
        expression = lParen.jjtGetChild(0);
        assertInstanceOf(ASTOr.class, expression);

        // the OR expression has two nodes ("property_2 eq 1" and "property_3 eq 3")
        assertEquals(2, expression.jjtGetNumChildren());

        // LHS - property comparison "property_2 eq 1"
        Node comparator = expression.jjtGetChild(0);
        assertInstanceOf(ASTComparison.class, comparator);
        comparison = (PredicateExpr) ((ASTComparison) comparator).jjtGetValue();
        assertEquals("property_2", comparison.getName());

        // RHS - property comparison "property_3 eq 3"
        comparator = expression.jjtGetChild(1);
        assertInstanceOf(ASTComparison.class, comparator);
        comparison = (PredicateExpr) ((ASTComparison) comparator).jjtGetValue();
        assertEquals("property_3", comparison.getName());
    }

    @Test
    public void testNotNull() throws Exception {
        Node filter = FilterParser.parse(context, "notnull(property)");
        assertNotNull(filter);

        List<PredicateExpr> comparisons = context.getComparisons();
        assertEquals(1, comparisons.size());

        PredicateExpr comparison = comparisons.get(0);
        assertEquals(BoolFunction.NOTNULL, comparison.getFunction());
        assertEquals("property", comparison.getName());
        assertNull(comparison.getOperator());
        assertNull(comparison.getValue());
        assertFalse(comparison.isNumeric());
    }

    @Test
    public void testIsNull() throws Exception {
        Node filter = FilterParser.parse(context, "isnull(property)");
        assertNotNull(filter);

        List<PredicateExpr> comparisons = context.getComparisons();
        assertEquals(1, comparisons.size());

        PredicateExpr comparison = comparisons.get(0);
        assertEquals(BoolFunction.ISNULL, comparison.getFunction());
        assertEquals("property", comparison.getName());
        assertNull(comparison.getOperator());
        assertNull(comparison.getValue());
        assertFalse(comparison.isNumeric());
    }

    @Test
    public void testToLower() throws Exception {
        Node filter = FilterParser.parse(context, "lower(property) eq 'abc'");
        assertNotNull(filter);

        List<PredicateExpr> comparisons = context.getComparisons();
        assertEquals(1, comparisons.size());

        PredicateExpr comparison = comparisons.get(0);
        assertEquals(UnaryFunction.LOWER, comparison.getFunction());
        assertEquals("property", comparison.getName());
        assertEquals(Operator.EQ, comparison.getOperator());
        assertEquals("abc", comparison.getValue());
        assertFalse(comparison.isNumeric());
    }

    @Test
    public void testToUpper() throws Exception {
        Node filter = FilterParser.parse(context, "upper(property) le 'abc'");
        assertNotNull(filter);

        List<PredicateExpr> comparisons = context.getComparisons();
        assertEquals(1, comparisons.size());

        PredicateExpr comparison = comparisons.get(0);
        assertEquals(UnaryFunction.UPPER, comparison.getFunction());
        assertEquals("property", comparison.getName());
        assertEquals(Operator.LE, comparison.getOperator());
        assertEquals("abc", comparison.getValue());
        assertFalse(comparison.isNumeric());
    }

    @Test
    public void testContains() throws Exception {
        Node filter = FilterParser.parse(context, "contains(property, 'abc')");
        assertNotNull(filter);

        List<PredicateExpr> comparisons = context.getComparisons();
        assertEquals(1, comparisons.size());

        PredicateExpr comparison = comparisons.get(0);
        assertEquals(BiFunction.CONTAINS, comparison.getFunction());
        assertEquals("property", comparison.getName());
        assertEquals("%abc%", comparison.getValue());
        assertFalse(comparison.isNumeric());
    }

    @Test
    public void testStartsWith() throws Exception {
        Node filter = FilterParser.parse(context, "startswith(property, 'abc')");
        assertNotNull(filter);

        List<PredicateExpr> comparisons = context.getComparisons();
        assertEquals(1, comparisons.size());

        PredicateExpr comparison = comparisons.get(0);
        assertEquals(BiFunction.STARTSWITH, comparison.getFunction());
        assertEquals("property", comparison.getName());
        assertEquals("abc%", comparison.getValue());
        assertFalse(comparison.isNumeric());
    }

    @Test
    public void testEndWith() throws Exception {
        Node filter = FilterParser.parse(context, "endswith(property, 'abc')");
        assertNotNull(filter);

        List<PredicateExpr> comparisons = context.getComparisons();
        assertEquals(1, comparisons.size());

        PredicateExpr comparison = comparisons.get(0);
        assertEquals(BiFunction.ENDSWITH, comparison.getFunction());
        assertEquals("property", comparison.getName());
        assertEquals("%abc", comparison.getValue());
        assertFalse(comparison.isNumeric());
    }

    @Test
    public void testEndWithNumeric() throws Exception {
        assertThrows(ParseException.class, () -> FilterParser.parse(context, "endswith(property, 999)"));
    }

    @Test
    public void testEndWithIdentifier() throws Exception {
        assertThrows(ParseException.class, () -> FilterParser.parse(context, "endswith(property, name)"));
    }

    @Test
    public void testFunctionCaseSensitive() throws Exception {
        assertThrows(ParseException.class, () -> FilterParser.parse(context, "iSNull(property)"));
        assertThrows(ParseException.class, () -> FilterParser.parse(context, "NULL(property)"));
        assertThrows(ParseException.class, () -> FilterParser.parse(context, "EndsWith(property, 'abc')"));
        assertThrows(ParseException.class, () -> FilterParser.parse(context, "StartsWith(property, 'abc')"));
        assertThrows(ParseException.class, () -> FilterParser.parse(context, "CONTAINS(property, 'abc')"));
        assertThrows(ParseException.class, () -> FilterParser.parse(context, "Upper(property) le 'abc')"));
        assertThrows(ParseException.class, () -> FilterParser.parse(context, "LOWER(property) le 'abc')"));
    }

    @Test
    public void testFunctionMissingParam() throws Exception {
        assertThrows(ParseException.class, () -> FilterParser.parse(context, "isnull()"));
        assertThrows(ParseException.class, () -> FilterParser.parse(context, "notnull()"));
    }

    @Test
    public void testFunctionMissingParamOne() throws Exception {
        assertThrows(ParseException.class, () -> FilterParser.parse(context, "endswith(a)"));
    }

    @Test
    public void testFunctionMissingParamTwo() throws Exception {
        assertThrows(ParseException.class, () -> FilterParser.parse(context, "endswith()"));
    }

    @Test
    public void testInvalidUnaryFunction() throws Exception {
        assertThrows(ParseException.class, () -> FilterParser.parse(context, "invalid(property) GT 'abc'"));
    }

    @Test
    public void testInvalidFunction() throws Exception {
        assertThrows(ParseException.class, () -> FilterParser.parse(context, "invalid(property,'abc')"));
    }

    /**
     * A mock implementation of QueryContext to keep a record of the PropertyComparisons generated
     * during parsing.
     */
    private static class MockQueryContext implements QueryContext {
        private final StringBuilder query = new StringBuilder();

        private final ArrayList<PredicateExpr> comparisons = new ArrayList<>();

        @Override
        public String getClassName() {
            return "mock";
        }

        public List<PredicateExpr> getComparisons() {
            return comparisons;
        }

        @Override
        public PredicateExpr newPredicate() {
            PredicateExpr result = new PredicateExpr(this);
            comparisons.add(result);

            return result;
        }

        @Override
        public Iterable<? extends PredicateExpr> getPredicates() {
            return null;
        }

        @Override
        public StringBuilder queryBuilder() {
            return query;
        }

        @Override
        public QueryProperty getPropertyFor(String aName) {
            return new QueryProperty(aName, "colName", String.class);
        }
    }
}
