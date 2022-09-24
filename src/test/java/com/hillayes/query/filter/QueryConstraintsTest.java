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

import com.hillayes.query.filter.exceptions.FilterExprException;
import com.hillayes.query.filter.exceptions.InvalidOrderByColException;
import com.hillayes.query.filter.exceptions.InvalidPropertyRefException;
import com.hillayes.query.filter.exceptions.OrderByConstructException;
import com.hillayes.query.filter.introspection.FilterProperty;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 * @author <a href="mailto:watson.phill@gmail.com">Phill Watson</a>
 * @since 1.0.0
 */
public class QueryConstraintsTest {
    @Test
    public void testConstructor() throws Exception {
        QueryConstraints fixture = new QueryConstraints(TestDataClass.class);
        assertNotNull(fixture);
    }

    @Test
    public void testisEmpty() throws Exception {
        QueryConstraints fixture = new QueryConstraints(TestDataClass.class);

        assertTrue(fixture.isEmpty());
    }

    @Test
    public void testSetSkip() throws Exception {
        QueryConstraints fixture = new QueryConstraints(TestDataClass.class);

        assertNull(fixture.getSkip());
        fixture.setSkip(10);

        assertNotNull(fixture.getSkip());
        assertEquals(10, fixture.getSkip().intValue());
        assertFalse(fixture.isEmpty());
    }

    @Test
    public void testSetSkipNegative() throws Exception {
        QueryConstraints fixture = new QueryConstraints(TestDataClass.class);

        assertNull(fixture.getSkip());
        fixture.setSkip(-10);

        assertNull(fixture.getSkip());
        assertTrue(fixture.isEmpty());
    }

    @Test
    public void testSetTop() throws Exception {
        QueryConstraints fixture = new QueryConstraints(TestDataClass.class);

        assertNull(fixture.getTop());
        fixture.setTop(10);

        assertNotNull(fixture.getTop());
        assertEquals(10, fixture.getTop().intValue());
        assertFalse(fixture.isEmpty());
    }

    @Test
    public void testSetTopNegative() throws Exception {
        QueryConstraints fixture = new QueryConstraints(TestDataClass.class);

        assertNull(fixture.getTop());
        fixture.setTop(-10);

        assertNull(fixture.getTop());
        assertTrue(fixture.isEmpty());
    }

    @Test
    public void testSetOrderEmpty() throws Exception {
        QueryConstraints fixture = new QueryConstraints(TestDataClass.class);

        assertNull(fixture.getOrderBy());
        fixture.setOrderBy("");

        assertNull(fixture.getOrderBy());
        assertTrue(fixture.isEmpty());
    }

    @Test
    public void testSetOrderNull() throws Exception {
        QueryConstraints fixture = new QueryConstraints(TestDataClass.class);

        assertNull(fixture.getOrderBy());
        fixture.setOrderBy(null);

        assertNull(fixture.getOrderBy());
        assertTrue(fixture.isEmpty());
    }

    @Test
    public void testFilter() throws Exception {
        QueryConstraints fixture = new QueryConstraints(TestDataClass.class);

        String expr = "propertyA EQ 'abc'";
        fixture.setFilter(expr);
        assertEquals(expr, fixture.getFilter());
        assertFalse(fixture.isEmpty());
    }

    @Test
    public void testFilterInvalidProp() throws Exception {
        PreparedStatement mockStatement = mock(PreparedStatement.class);

        Connection mockConnection = mock(Connection.class);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);

        QueryConstraints fixture = new QueryConstraints(TestDataClass.class);

        fixture.setFilter("propertyA EQ 'abc' and propertyX EQ 'abc' or propertyB EQ 'abc'");
        try {
            fixture.prepareStatement(mockConnection, "select x from table");
            fail("Expected InvalidPropertyRefException");
        } catch (InvalidPropertyRefException expected) {
            assertEquals("propertyX", expected.getPropertyName());
        }
    }

    @Test
    public void testFilterParseException() throws Exception {
        PreparedStatement mockStatement = mock(PreparedStatement.class);

        Connection mockConnection = mock(Connection.class);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);

        QueryConstraints fixture = new QueryConstraints(TestDataClass.class);

        String expr = "propertyA EQ 'abc' and propertyC EQ or propertyB EQ 'abc'";
        fixture.setFilter(expr);
        try {
            fixture.prepareStatement(mockConnection, "select x from table");
            fail("Expected FilterExprException");
        } catch (FilterExprException expected) {
            assertEquals(expr, expected.getFilterExpr());
        }
    }

    @Test
    public void testPrepareStatementFilter() throws Exception {
        PreparedStatement mockStatement = mock(PreparedStatement.class);

        Connection mockConnection = mock(Connection.class);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);

        QueryConstraints fixture = new QueryConstraints(TestDataClass.class);

        String expr = "propertyA EQ 'abc' or propertyB EQ 10";

        fixture.setFilter(expr);
        assertNotNull(fixture.prepareStatement(mockConnection, "select x, y from table"));

        verify(mockConnection).prepareStatement("select x, y from table WHERE propertyA = ? OR propertyB = ?");
    }

    @Test
    public void testPrepareStatementSkip() throws Exception {
        PreparedStatement mockStatement = mock(PreparedStatement.class);

        Connection mockConnection = mock(Connection.class);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);

        QueryConstraints fixture = new QueryConstraints(TestDataClass.class);

        fixture.setSkip(10);
        assertNotNull(fixture.prepareStatement(mockConnection, "select x, y from table"));

        verify(mockConnection).prepareStatement("select x, y from table OFFSET 10");
    }

    @Test
    public void testPrepareStatementTop() throws Exception {
        PreparedStatement mockStatement = mock(PreparedStatement.class);

        Connection mockConnection = mock(Connection.class);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);

        QueryConstraints fixture = new QueryConstraints(TestDataClass.class);

        fixture.setTop(10);
        assertNotNull(fixture.prepareStatement(mockConnection, "select x, y from table"));

        verify(mockConnection).prepareStatement("select x, y from table LIMIT 10");
    }

    @Test
    public void testPrepareStatementOrderBy() throws Exception {
        PreparedStatement mockStatement = mock(PreparedStatement.class);

        Connection mockConnection = mock(Connection.class);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);

        QueryConstraints fixture = new QueryConstraints(TestDataClass.class);

        assertNull(fixture.getOrderBy());
        fixture.setOrderBy("propertyA asc, propertyB desc, propertyC asc");
        assertNotNull(fixture.getOrderBy());
        assertFalse(fixture.isEmpty());

        assertNotNull(fixture.prepareStatement(mockConnection, "select x, y from table"));

        verify(mockConnection).prepareStatement("select x, y from table ORDER BY propertyA ASC, propertyB DESC, propertyC ASC");
    }

    @Test
    public void testPrepareStatementOrderByDefaultOrder() throws Exception {
        PreparedStatement mockStatement = mock(PreparedStatement.class);

        Connection mockConnection = mock(Connection.class);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);

        QueryConstraints fixture = new QueryConstraints(TestDataClass.class);

        assertNull(fixture.getOrderBy());
        fixture.setOrderBy("propertyA");
        assertNotNull(fixture.getOrderBy());
        assertFalse(fixture.isEmpty());

        assertNotNull(fixture.prepareStatement(mockConnection, "select x, y from table"));

        verify(mockConnection).prepareStatement("select x, y from table ORDER BY propertyA ASC");
    }

    @Test
    public void testPrepareStatementOrderByEmptyElement() throws Exception {
        PreparedStatement mockStatement = mock(PreparedStatement.class);

        Connection mockConnection = mock(Connection.class);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);

        QueryConstraints fixture = new QueryConstraints(TestDataClass.class);

        assertNull(fixture.getOrderBy());
        fixture.setOrderBy("propertyA asc, , propertyB desc");
        assertNotNull(fixture.getOrderBy());
        assertFalse(fixture.isEmpty());

        assertNotNull(fixture.prepareStatement(mockConnection, "select x, y from table"));

        verify(mockConnection).prepareStatement("select x, y from table ORDER BY propertyA ASC, propertyB DESC");
    }

    @Test
    public void testPrepareStatementOrderByMultiple() throws Exception {
        PreparedStatement mockStatement = mock(PreparedStatement.class);

        Connection mockConnection = mock(Connection.class);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);

        QueryConstraints fixture = new QueryConstraints(TestDataClass.class);

        assertNull(fixture.getOrderBy());
        fixture.setOrderBy("propertyA asc, propertyB desc, propertyC asc");
        assertNotNull(fixture.getOrderBy());
        assertFalse(fixture.isEmpty());

        assertNotNull(fixture.prepareStatement(mockConnection, "select x, y from table"));

        verify(mockConnection).prepareStatement("select x, y from table ORDER BY propertyA ASC, propertyB DESC, propertyC ASC");
    }

    @Test
    public void testPrepareStatementOrderByInvalidProp() throws Exception {
        PreparedStatement mockStatement = mock(PreparedStatement.class);

        Connection mockConnection = mock(Connection.class);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);

        QueryConstraints fixture = new QueryConstraints(TestDataClass.class);

        assertNull(fixture.getOrderBy());
        fixture.setOrderBy("propertyA asc, propertyX desc, propertyC asc");
        assertNotNull(fixture.getOrderBy());
        assertFalse(fixture.isEmpty());

        try {
            fixture.prepareStatement(mockConnection, "select x, y from table");
            fail("Expected InvalidOrderByColException");
        } catch (InvalidOrderByColException expected) {
            assertEquals("propertyX", expected.getInvalidColumn());
        }
    }

    @Test
    public void testPrepareStatementOrderByInvalidConstruct() throws Exception {
        PreparedStatement mockStatement = mock(PreparedStatement.class);

        Connection mockConnection = mock(Connection.class);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);

        QueryConstraints fixture = new QueryConstraints(TestDataClass.class);

        assertNull(fixture.getOrderBy());
        fixture.setOrderBy("propertyA asc asc");
        assertNotNull(fixture.getOrderBy());
        assertFalse(fixture.isEmpty());

        try {
            fixture.prepareStatement(mockConnection, "select x, y from table");
            fail("Expected OrderByConstructException");
        } catch (OrderByConstructException expected) {
            assertEquals("propertyA asc asc", expected.getOrderBy());
        }
    }

    @Test
    public void testPrepareStatementOrderByInvalidOrder() throws Exception {
        PreparedStatement mockStatement = mock(PreparedStatement.class);

        Connection mockConnection = mock(Connection.class);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);

        QueryConstraints fixture = new QueryConstraints(TestDataClass.class);

        assertNull(fixture.getOrderBy());
        fixture.setOrderBy("propertyA backward");
        assertNotNull(fixture.getOrderBy());
        assertFalse(fixture.isEmpty());

        try {
            fixture.prepareStatement(mockConnection, "select x, y from table");
            fail("Expected OrderByConstructException");
        } catch (OrderByConstructException expected) {
            assertEquals("propertyA backward", expected.getOrderBy());
        }
    }

    @Test
    public void testPrepareStatementAll() throws Exception {
        PreparedStatement mockStatement = mock(PreparedStatement.class);

        Connection mockConnection = mock(Connection.class);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);

        QueryConstraints fixture = new QueryConstraints(TestDataClass.class);

        String expr = "propertyA EQ 'abc' or propertyB EQ 10";

        fixture.setFilter(expr);
        fixture.setSkip(10);
        fixture.setTop(10);
        fixture.setOrderBy("propertyC");
        assertNotNull(fixture.prepareStatement(mockConnection, "select x, y from table"));

        verify(mockConnection).prepareStatement("select x, y from table WHERE propertyA = ? OR propertyB = ? ORDER BY propertyC ASC OFFSET 10 LIMIT 10");
    }

    public interface TestDataClass {
        @FilterProperty
        String getPropertyA();

        @FilterProperty
        byte getPropertyB();

        @FilterProperty
        int getPropertyC();
    }
}
