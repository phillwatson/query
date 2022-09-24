/**
 * [Phillip Watson] ("COMPANY") CONFIDENTIAL Unpublished Copyright © 2019-2020 Phillip Watson,
 * All Rights Reserved.
 *
 * NOTICE: All information contained herein is, and remains the property of COMPANY. The
 * intellectual and technical concepts contained herein are proprietary to COMPANY and may be
 * covered by U.K. and Foreign Patents, patents in process, and are protected by trade secret or
 * copyright law. Dissemination of this information or reproduction of this material is strictly
 * forbidden unless prior written permission is obtained from COMPANY. Access to the source code
 * contained herein is hereby forbidden to anyone except current COMPANY employees, managers or
 * contractors who have executed Confidentiality and Non-disclosure agreements explicitly covering
 * such access.
 *
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

import com.hillayes.query.filter.introspection.Property;
import com.hillayes.query.filter.parser.ASTAnd;
import com.hillayes.query.filter.parser.ASTComparison;
import com.hillayes.query.filter.parser.ASTNot;
import com.hillayes.query.filter.parser.ASTOr;
import com.hillayes.query.filter.parser.ASTparse;
import com.hillayes.query.filter.parser.FilterParser;
import com.hillayes.query.filter.parser.FilterParserVisitor;
import com.hillayes.query.filter.parser.Node;
import com.hillayes.query.filter.parser.SimpleNode;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 *
 * @author <a href="mailto:watson.phill@gmail.com">Phill Watson</a>
 * @since 1.0.0
 */
public class FilterParserVisitorTest
{
    @Test
    public void testNotComplex() throws Exception
    {
        MockQueryContext context = new MockQueryContext();

        Node filter = FilterParser.parse(context, "not (property le 2 or property gt 4) and property eq 3");
        assertNotNull(filter);

        Visitor visitor = new Visitor();
        filter.jjtAccept(visitor, null);

        List<Node> visited = visitor.getVisited();
        assertEquals(7, visited.size());

        assertTrue("was " + visited.get(0).getClass().getSimpleName(), visited.get(0) instanceof ASTparse);
        assertTrue("was " + visited.get(1).getClass().getSimpleName(), visited.get(1) instanceof ASTNot);
        assertTrue("was " + visited.get(2).getClass().getSimpleName(), visited.get(2) instanceof ASTAnd);
        assertTrue("was " + visited.get(3).getClass().getSimpleName(), visited.get(3) instanceof ASTOr);
        assertTrue("was " + visited.get(4).getClass().getSimpleName(), visited.get(4) instanceof ASTComparison);
        assertTrue("was " + visited.get(5).getClass().getSimpleName(), visited.get(5) instanceof ASTComparison);
        assertTrue("was " + visited.get(6).getClass().getSimpleName(), visited.get(6) instanceof ASTComparison);
    }

    @Test
    public void testMultiAnd() throws Exception
    {
        MockQueryContext context = new MockQueryContext();

        Node filter = FilterParser.parse(context, "property le 2 and property gt 4 and property eq 3");
        assertNotNull(filter);

        Visitor visitor = new Visitor();
        filter.jjtAccept(visitor, null);

        List<Node> visited = visitor.getVisited();
        assertEquals(5, visited.size());

        assertTrue("was " + visited.get(0).getClass().getSimpleName(), visited.get(0) instanceof ASTparse);
        assertTrue("was " + visited.get(1).getClass().getSimpleName(), visited.get(1) instanceof ASTAnd);
        assertTrue("was " + visited.get(2).getClass().getSimpleName(), visited.get(2) instanceof ASTComparison);
        assertTrue("was " + visited.get(3).getClass().getSimpleName(), visited.get(3) instanceof ASTComparison);
        assertTrue("was " + visited.get(4).getClass().getSimpleName(), visited.get(4) instanceof ASTComparison);
    }

    @Test
    public void testMultiOr() throws Exception
    {
        MockQueryContext context = new MockQueryContext();

        Node filter = FilterParser.parse(context, "property le 2 or property gt 4 or property eq 3");
        assertNotNull(filter);

        Visitor visitor = new Visitor();
        filter.jjtAccept(visitor, null);

        List<Node> visited = visitor.getVisited();
        assertEquals(5, visited.size());

        assertTrue("was " + visited.get(0).getClass().getSimpleName(), visited.get(0) instanceof ASTparse);
        assertTrue("was " + visited.get(1).getClass().getSimpleName(), visited.get(1) instanceof ASTOr);
        assertTrue("was " + visited.get(2).getClass().getSimpleName(), visited.get(2) instanceof ASTComparison);
        assertTrue("was " + visited.get(3).getClass().getSimpleName(), visited.get(3) instanceof ASTComparison);
        assertTrue("was " + visited.get(4).getClass().getSimpleName(), visited.get(4) instanceof ASTComparison);
    }

    @Test
    public void testMixAndOr() throws Exception
    {
        MockQueryContext context = new MockQueryContext();

        Node filter = FilterParser.parse(context, "property le 2 and property gt 4 or property eq 3 and property ne 5");
        assertNotNull(filter);

        Visitor visitor = new Visitor();
        filter.jjtAccept(visitor, null);

        List<Node> visited = visitor.getVisited();
        assertEquals(8, visited.size());

        assertTrue("was " + visited.get(0).getClass().getSimpleName(), visited.get(0) instanceof ASTparse);
        assertTrue("was " + visited.get(1).getClass().getSimpleName(), visited.get(1) instanceof ASTOr);
        assertTrue("was " + visited.get(2).getClass().getSimpleName(), visited.get(2) instanceof ASTAnd);
        assertTrue("was " + visited.get(3).getClass().getSimpleName(), visited.get(3) instanceof ASTComparison);
        assertTrue("was " + visited.get(4).getClass().getSimpleName(), visited.get(4) instanceof ASTComparison);
        assertTrue("was " + visited.get(5).getClass().getSimpleName(), visited.get(5) instanceof ASTAnd);
        assertTrue("was " + visited.get(6).getClass().getSimpleName(), visited.get(6) instanceof ASTComparison);
        assertTrue("was " + visited.get(7).getClass().getSimpleName(), visited.get(7) instanceof ASTComparison);
    }

    @Test
    public void testMixAndOrBrackets() throws Exception
    {
        MockQueryContext context = new MockQueryContext();

        Node filter = FilterParser.parse(context,
                                         "(property le 2 and property gt 4 or property eq 3) and property ne 5");
        assertNotNull(filter);

        Visitor visitor = new Visitor();
        filter.jjtAccept(visitor, null);

        List<Node> visited = visitor.getVisited();
        assertEquals(8, visited.size());

        assertTrue("was " + visited.get(0).getClass().getSimpleName(), visited.get(0) instanceof ASTparse);
        assertTrue("was " + visited.get(1).getClass().getSimpleName(), visited.get(1) instanceof ASTAnd);
        assertTrue("was " + visited.get(2).getClass().getSimpleName(), visited.get(2) instanceof ASTOr);
        assertTrue("was " + visited.get(3).getClass().getSimpleName(), visited.get(3) instanceof ASTAnd);
        assertTrue("was " + visited.get(4).getClass().getSimpleName(), visited.get(4) instanceof ASTComparison);
        assertTrue("was " + visited.get(5).getClass().getSimpleName(), visited.get(5) instanceof ASTComparison);
        assertTrue("was " + visited.get(6).getClass().getSimpleName(), visited.get(6) instanceof ASTComparison);
        assertTrue("was " + visited.get(7).getClass().getSimpleName(), visited.get(7) instanceof ASTComparison);
    }

    @Test
    public void testMixOrAndBrackets() throws Exception
    {
        MockQueryContext context = new MockQueryContext();

        Node filter = FilterParser.parse(context,
                                         "property le 2 and (property gt 4 or property eq 3 and property ne 5)");
        assertNotNull(filter);

        Visitor visitor = new Visitor();
        filter.jjtAccept(visitor, null);

        List<Node> visited = visitor.getVisited();
        assertEquals(8, visited.size());

        assertTrue("was " + visited.get(0).getClass().getSimpleName(), visited.get(0) instanceof ASTparse);
        assertTrue("was " + visited.get(1).getClass().getSimpleName(), visited.get(1) instanceof ASTAnd);
        assertTrue("was " + visited.get(2).getClass().getSimpleName(), visited.get(2) instanceof ASTComparison);
        assertTrue("was " + visited.get(3).getClass().getSimpleName(), visited.get(3) instanceof ASTOr);
        assertTrue("was " + visited.get(4).getClass().getSimpleName(), visited.get(4) instanceof ASTComparison);
        assertTrue("was " + visited.get(5).getClass().getSimpleName(), visited.get(5) instanceof ASTAnd);
        assertTrue("was " + visited.get(6).getClass().getSimpleName(), visited.get(6) instanceof ASTComparison);
        assertTrue("was " + visited.get(7).getClass().getSimpleName(), visited.get(7) instanceof ASTComparison);
    }

    @Test
    public void testMixOrAndBrackets2() throws Exception
    {
        MockQueryContext context = new MockQueryContext();

        // precedence will be given to the OR
        Node filter = FilterParser.parse(context,
                                         "property le 2 and (property gt 4 or property eq 3) and property ne 5");
        assertNotNull(filter);

        Visitor visitor = new Visitor();
        filter.jjtAccept(visitor, null);

        List<Node> visited = visitor.getVisited();
        assertEquals(7, visited.size());

        assertTrue("was " + visited.get(0).getClass().getSimpleName(), visited.get(0) instanceof ASTparse);
        assertTrue("was " + visited.get(1).getClass().getSimpleName(), visited.get(1) instanceof ASTAnd);
        assertTrue("was " + visited.get(2).getClass().getSimpleName(), visited.get(2) instanceof ASTComparison);
        assertTrue("was " + visited.get(3).getClass().getSimpleName(), visited.get(3) instanceof ASTOr);
        assertTrue("was " + visited.get(4).getClass().getSimpleName(), visited.get(4) instanceof ASTComparison);
        assertTrue("was " + visited.get(5).getClass().getSimpleName(), visited.get(5) instanceof ASTComparison);
        assertTrue("was " + visited.get(6).getClass().getSimpleName(), visited.get(6) instanceof ASTComparison);
    }

    private static class Visitor implements FilterParserVisitor
    {
        private List<Node> visited = new ArrayList<>();

        public List<Node> getVisited()
        {
            return visited;
        }

        @Override
        public Object visit(SimpleNode aNode, Object aData)
        {
            // never used
            throw new RuntimeException("Didn't expect this visit.");
        }

        @Override
        public Object visit(ASTparse aNode, Object aData)
        {
            visited.add(aNode);
            aNode.childrenAccept(this, aData);
            return null;
        }

        @Override
        public Object visit(ASTOr aNode, Object aData)
        {
            visited.add(aNode);
            aNode.childrenAccept(this, aData);
            return null;
        }

        @Override
        public Object visit(ASTAnd aNode, Object aData)
        {
            visited.add(aNode);
            aNode.childrenAccept(this, aData);
            return null;
        }

        @Override
        public Object visit(ASTNot aNode, Object aData)
        {
            visited.add(aNode);
            aNode.childrenAccept(this, aData);
            return null;
        }

        @Override
        public Object visit(ASTComparison aNode, Object aData)
        {
            visited.add(aNode);
            aNode.childrenAccept(this, aData);
            return null;
        }
    }

    private void assertTrue(String aMessage, boolean aValue)
    {
        org.junit.jupiter.api.Assertions.assertTrue(aValue, aMessage);
    }

    /**
     * A mock implementation of QueryContext to keep a record of the PropertyComparisons generated
     * during parsing.
     */
    private class MockQueryContext implements QueryContext
    {
        private StringBuilder query = new StringBuilder();

        @Override
        public Predicate newPredicate()
        {
            return new Predicate(this);
        }

        @Override
        public StringBuilder queryBuilder()
        {
            return query;
        }

        @Override
        public Property getPropertyFor(String aName)
        {
            return Mockito.mock(Property.class);
        }

        @Override
        public void applyArg(PreparedStatement aStatement, int aArgIndex) throws SQLException
        {
        }
    }
}
