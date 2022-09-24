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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 * @author <a href="mailto:watson.phill@gmail.com">Phill Watson</a>
 * @since 1.0.0
 */
public class QueryConstraintsStringTest
{
    private Connection mockConnection;

    private QueryConstraints fixture;

    @BeforeEach
    public void initMocks() throws Exception
    {
        PreparedStatement mockStatement = mock(PreparedStatement.class);

        mockConnection = mock(Connection.class);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);

        fixture = new QueryConstraints(TestDataClass.class);
    }

    @Test
    public void test() throws Exception
    {
        fixture.setFilter("propertyA EQ 'abc'");
        PreparedStatement statement = fixture.prepareStatement(mockConnection, "select a from table");
        assertNotNull(statement);

        verify(mockConnection).prepareStatement("select a from table WHERE a = ?");
        verify(statement).setString(1, "abc");
    }

    @Test
    public void testStartsWith() throws Exception
    {
        fixture.setFilter("startswith(propertyA, 'ABC')");
        PreparedStatement statement = fixture.prepareStatement(mockConnection, "select a from table");
        assertNotNull(statement);

        verify(mockConnection).prepareStatement("select a from table WHERE a like ?");
        verify(statement).setString(1, "abc%");
    }

    @Test
    public void testEndsWith() throws Exception
    {
        fixture.setFilter("endswith(propertyA, 'AbC')");
        PreparedStatement statement = fixture.prepareStatement(mockConnection, "select a from table");
        assertNotNull(statement);

        verify(mockConnection).prepareStatement("select a from table WHERE a like ?");
        verify(statement).setString(1, "%abc");
    }

    @Test
    public void testContains() throws Exception
    {
        fixture.setFilter("contains(propertyA, 'AbC')");
        PreparedStatement statement = fixture.prepareStatement(mockConnection, "select a from table");
        assertNotNull(statement);

        verify(mockConnection).prepareStatement("select a from table WHERE a like ?");
        verify(statement).setString(1, "%abc%");
    }

    @Test
    public void testLower() throws Exception
    {
        fixture.setFilter("lower(propertyA) eq 'AbC'");
        PreparedStatement statement = fixture.prepareStatement(mockConnection, "select a from table");
        assertNotNull(statement);

        verify(mockConnection).prepareStatement("select a from table WHERE LOWER(a) = ?");
        verify(statement).setString(1, "AbC");
    }

    @Test
    public void testUpper() throws Exception
    {
        fixture.setFilter("upper(propertyA) eq 'AbC'");
        PreparedStatement statement = fixture.prepareStatement(mockConnection, "select a from table");
        assertNotNull(statement);

        verify(mockConnection).prepareStatement("select a from table WHERE UPPER(a) = ?");
        verify(statement).setString(1, "AbC");
    }

    @Test
    public void testIsNull() throws Exception
    {
        fixture.setFilter("isnull(propertyA)");
        PreparedStatement statement = fixture.prepareStatement(mockConnection, "select a from table");
        assertNotNull(statement);

        verify(mockConnection).prepareStatement("select a from table WHERE a is null");
        verify(statement, never()).setString(anyInt(), anyString());
    }

    @Test
    public void testNotNull() throws Exception
    {
        fixture.setFilter("notnull(propertyA)");
        PreparedStatement statement = fixture.prepareStatement(mockConnection, "select a from table");
        assertNotNull(statement);

        verify(mockConnection).prepareStatement("select a from table WHERE a is not null");
        verify(statement, never()).setString(anyInt(), anyString());
    }

    @Test
    public void testNotEq() throws Exception
    {
        fixture.setFilter("propertyA ne \"abc\"");
        PreparedStatement statement = fixture.prepareStatement(mockConnection, "select x from table");
        assertNotNull(statement);

        verify(mockConnection).prepareStatement("select x from table WHERE a <> ?");
        verify(statement).setString(1, "abc");
    }

    @Test
    public void testLessThan() throws Exception
    {
        fixture.setFilter("propertyA lt \"abc\"");
        PreparedStatement statement = fixture.prepareStatement(mockConnection, "select x from table");
        assertNotNull(statement);

        verify(mockConnection).prepareStatement("select x from table WHERE a < ?");
        verify(statement).setString(1, "abc");
    }

    @Test
    public void testLessThanEqual() throws Exception
    {
        fixture.setFilter("propertyA le \"abc\"");
        PreparedStatement statement = fixture.prepareStatement(mockConnection, "select x from table");
        assertNotNull(statement);

        verify(mockConnection).prepareStatement("select x from table WHERE a <= ?");
        verify(statement).setString(1, "abc");
    }

    @Test
    public void testGreaterThanEqual() throws Exception
    {
        fixture.setFilter("propertyA ge \"abc\"");
        PreparedStatement statement = fixture.prepareStatement(mockConnection, "select x from table");
        assertNotNull(statement);

        verify(mockConnection).prepareStatement("select x from table WHERE a >= ?");
        verify(statement).setString(1, "abc");
    }

    @Test
    public void testGreaterThan() throws Exception
    {
        fixture.setFilter("propertyA gt \"abc\"");
        PreparedStatement statement = fixture.prepareStatement(mockConnection, "select x from table");
        assertNotNull(statement);

        verify(mockConnection).prepareStatement("select x from table WHERE a > ?");
        verify(statement).setString(1, "abc");
    }

    public static interface TestDataClass
    {
        @FilterProperty(colname = "a")
        public String getPropertyA();
    }
}
