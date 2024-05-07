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
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author <a href="mailto:watson.phill@gmail.com">Phill Watson</a>
 * @since 1.0.0
 */
public class FilterParserVisitorTest {
    @Test
    public void testNotComplex() throws Exception {
        MockQueryContext context = new MockQueryContext();

        Node filter = FilterParser.parse(context, "not (property le 2 or property gt 4) and property eq 3");
        assertNotNull(filter);

        Visitor visitor = new Visitor();
        filter.jjtAccept(visitor, null);

        List<Node> visited = visitor.getVisited();
        assertEquals(7, visited.size());

        assertInstanceOf(ASTparse.class, visited.get(0));
        assertInstanceOf(ASTNot.class, visited.get(1));
        assertInstanceOf(ASTAnd.class, visited.get(2));
        assertInstanceOf(ASTOr.class, visited.get(3));
        assertInstanceOf(ASTComparison.class, visited.get(4));
        assertInstanceOf(ASTComparison.class, visited.get(5));
        assertInstanceOf(ASTComparison.class, visited.get(6));
    }

    @Test
    public void testMultiAnd() throws Exception {
        MockQueryContext context = new MockQueryContext();

        Node filter = FilterParser.parse(context, "property le 2 and property gt 4 and property eq 3");
        assertNotNull(filter);

        Visitor visitor = new Visitor();
        filter.jjtAccept(visitor, null);

        List<Node> visited = visitor.getVisited();
        assertEquals(5, visited.size());

        assertInstanceOf(ASTparse.class, visited.get(0));
        assertInstanceOf(ASTAnd.class, visited.get(1));
        assertInstanceOf(ASTComparison.class, visited.get(2));
        assertInstanceOf(ASTComparison.class, visited.get(3));
        assertInstanceOf(ASTComparison.class, visited.get(4));
    }

    @Test
    public void testMultiOr() throws Exception {
        MockQueryContext context = new MockQueryContext();

        Node filter = FilterParser.parse(context, "property le 2 or property gt 4 or property eq 3");
        assertNotNull(filter);

        Visitor visitor = new Visitor();
        filter.jjtAccept(visitor, null);

        List<Node> visited = visitor.getVisited();
        assertEquals(5, visited.size());

        assertInstanceOf(ASTparse.class, visited.get(0));
        assertInstanceOf(ASTOr.class, visited.get(1));
        assertInstanceOf(ASTComparison.class, visited.get(2));
        assertInstanceOf(ASTComparison.class, visited.get(3));
        assertInstanceOf(ASTComparison.class, visited.get(4));
    }

    @Test
    public void testMixAndOr() throws Exception {
        MockQueryContext context = new MockQueryContext();

        Node filter = FilterParser.parse(context, "property le 2 and property gt 4 or property eq 3 and property ne 5");
        assertNotNull(filter);

        Visitor visitor = new Visitor();
        filter.jjtAccept(visitor, null);

        List<Node> visited = visitor.getVisited();
        assertEquals(8, visited.size());

        assertInstanceOf(ASTparse.class, visited.get(0));
        assertInstanceOf(ASTOr.class, visited.get(1));
        assertInstanceOf(ASTAnd.class, visited.get(2));
        assertInstanceOf(ASTComparison.class, visited.get(3));
        assertInstanceOf(ASTComparison.class, visited.get(4));
        assertInstanceOf(ASTAnd.class, visited.get(5));
        assertInstanceOf(ASTComparison.class, visited.get(6));
        assertInstanceOf(ASTComparison.class, visited.get(7));
    }

    @Test
    public void testMixAndOrBrackets() throws Exception {
        MockQueryContext context = new MockQueryContext();

        Node filter = FilterParser.parse(context,
            "(property le 2 and property gt 4 or property eq 3) and property ne 5");
        assertNotNull(filter);

        Visitor visitor = new Visitor();
        filter.jjtAccept(visitor, null);

        List<Node> visited = visitor.getVisited();
        assertEquals(8, visited.size());

        assertInstanceOf(ASTparse.class, visited.get(0));
        assertInstanceOf(ASTAnd.class, visited.get(1));
        assertInstanceOf(ASTOr.class, visited.get(2));
        assertInstanceOf(ASTAnd.class, visited.get(3));
        assertInstanceOf(ASTComparison.class, visited.get(4));
        assertInstanceOf(ASTComparison.class, visited.get(5));
        assertInstanceOf(ASTComparison.class, visited.get(6));
        assertInstanceOf(ASTComparison.class, visited.get(7));
    }

    @Test
    public void testMixOrAndBrackets() throws Exception {
        MockQueryContext context = new MockQueryContext();

        Node filter = FilterParser.parse(context,
            "property le 2 and (property gt 4 or property eq 3 and property ne 5)");
        assertNotNull(filter);

        Visitor visitor = new Visitor();
        filter.jjtAccept(visitor, null);

        List<Node> visited = visitor.getVisited();
        assertEquals(8, visited.size());

        assertInstanceOf(ASTparse.class, visited.get(0));
        assertInstanceOf(ASTAnd.class, visited.get(1));
        assertInstanceOf(ASTComparison.class, visited.get(2));
        assertInstanceOf(ASTOr.class, visited.get(3));
        assertInstanceOf(ASTComparison.class, visited.get(4));
        assertInstanceOf(ASTAnd.class, visited.get(5));
        assertInstanceOf(ASTComparison.class, visited.get(6));
        assertInstanceOf(ASTComparison.class, visited.get(7));
    }

    @Test
    public void testMixOrAndBrackets2() throws Exception {
        MockQueryContext context = new MockQueryContext();

        // precedence will be given to the OR
        Node filter = FilterParser.parse(context,
            "property le 2 and (property gt 4 or property eq 3) and property ne 5");
        assertNotNull(filter);

        Visitor visitor = new Visitor();
        filter.jjtAccept(visitor, null);

        List<Node> visited = visitor.getVisited();
        assertEquals(7, visited.size());

        assertInstanceOf(ASTparse.class, visited.get(0));
        assertInstanceOf(ASTAnd.class, visited.get(1));
        assertInstanceOf(ASTComparison.class, visited.get(2));
        assertInstanceOf(ASTOr.class, visited.get(3));
        assertInstanceOf(ASTComparison.class, visited.get(4));
        assertInstanceOf(ASTComparison.class, visited.get(5));
        assertInstanceOf(ASTComparison.class, visited.get(6));
    }

    private static class Visitor extends FilterParserDefaultVisitor {
        private final List<Node> visited = new ArrayList<>();

        public List<Node> getVisited() {
            return visited;
        }

        @Override
        public com.hillayes.query.filter.QueryPredicate visit(SimpleNode aNode, com.hillayes.query.filter.QueryPredicate aData) {
            // never used
            throw new RuntimeException("Didn't expect this visit.");
        }

        @Override
        public com.hillayes.query.filter.QueryPredicate visit(ASTparse aNode, com.hillayes.query.filter.QueryPredicate aData) {
            visited.add(aNode);
            aNode.childrenAccept(this, aData);
            return null;
        }

        @Override
        public com.hillayes.query.filter.QueryPredicate visit(ASTOr aNode, com.hillayes.query.filter.QueryPredicate aData) {
            visited.add(aNode);
            aNode.childrenAccept(this, aData);
            return null;
        }

        @Override
        public com.hillayes.query.filter.QueryPredicate visit(ASTAnd aNode, com.hillayes.query.filter.QueryPredicate aData) {
            visited.add(aNode);
            aNode.childrenAccept(this, aData);
            return null;
        }

        @Override
        public com.hillayes.query.filter.QueryPredicate visit(ASTNot aNode, com.hillayes.query.filter.QueryPredicate aData) {
            visited.add(aNode);
            aNode.childrenAccept(this, aData);
            return null;
        }

        @Override
        public com.hillayes.query.filter.QueryPredicate visit(ASTComparison aNode, com.hillayes.query.filter.QueryPredicate aData) {
            visited.add(aNode);
            aNode.childrenAccept(this, aData);
            return null;
        }
    }

    /**
     * A mock implementation of QueryContext to keep a record of the PropertyComparisons generated
     * during parsing.
     */
    private static class MockQueryContext implements QueryContext {
        private final StringBuilder query = new StringBuilder();

        @Override
        public String getClassName() {
            return "mock";
        }

        @Override
        public PredicateExpr newPredicate() {
            return new PredicateExpr(this);
        }

        @Override
        public StringBuilder queryBuilder() {
            return query;
        }

        @Override
        public QueryProperty getPropertyFor(String aName) {
            return Mockito.mock(QueryProperty.class);
        }

        @Override
        public Iterable<? extends PredicateExpr> getPredicates() {
            return null;
        }
    }
}
