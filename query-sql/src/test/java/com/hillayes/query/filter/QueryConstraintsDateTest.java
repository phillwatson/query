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

import com.hillayes.query.filter.introspection.DataClassQueryContext;
import com.hillayes.query.filter.introspection.FilterProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.Instant;
import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * @author <a href="mailto:watson.phill@gmail.com">Phill Watson</a>
 * @since 1.0.0
 */
public class QueryConstraintsDateTest {
    private static final String DATE_STR = "2001-10-15T21:22:30Z";

    private static final Instant CALENDAR_VAL = Instant.parse(DATE_STR);

    private Connection mockConnection;

    private QueryConstraints fixture;

    @BeforeEach
    public void initMocks() throws Exception {
        PreparedStatement mockStatement = mock(PreparedStatement.class);

        mockConnection = mock(Connection.class);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);

        fixture = new QueryConstraints(new DataClassQueryContext(TestDataClass.class));
    }

    @Test
    public void testFormatException() throws Exception {
        fixture.setFilter("propertyA EQ 'abc'");

        assertThrows(DateTimeParseException.class, () -> fixture.prepareStatement(mockConnection, "select x from table"));
    }

    @Test
    public void testUtilDate() throws Exception {
        fixture.setFilter("propertyA eq \"" + DATE_STR + "\"");
        PreparedStatement statement = fixture.prepareStatement(mockConnection, "select x from table");
        assertNotNull(statement);

        verify(mockConnection).prepareStatement("select x from table WHERE a = ?");
        verify(statement).setDate(1, new java.sql.Date(CALENDAR_VAL.toEpochMilli()));
    }

    @Test
    public void testSqlDate() throws Exception {
        fixture.setFilter("propertyB eq \"" + DATE_STR + "\"");
        PreparedStatement statement = fixture.prepareStatement(mockConnection, "select x from table");
        assertNotNull(statement);

        verify(mockConnection).prepareStatement("select x from table WHERE b = ?");
        verify(statement).setDate(1, new java.sql.Date(CALENDAR_VAL.toEpochMilli()));
    }

    @Test
    public void testIsNull() throws Exception {
        fixture.setFilter("isnull(propertyA)");
        PreparedStatement statement = fixture.prepareStatement(mockConnection, "select x from table");
        assertNotNull(statement);

        verify(mockConnection).prepareStatement("select x from table WHERE a is null");
        verify(statement, never()).setDate(anyInt(), any());
        verifyNoMoreInteractions(statement);
    }

    @Test
    public void testNotNull() throws Exception {
        fixture.setFilter("notnull(propertyA)");
        PreparedStatement statement = fixture.prepareStatement(mockConnection, "select x from table");
        assertNotNull(statement);

        verify(mockConnection).prepareStatement("select x from table WHERE a is not null");
        verify(statement, never()).setDate(anyInt(), any());
        verifyNoMoreInteractions(statement);
    }

    @Test
    public void testNotEq() throws Exception {
        fixture.setFilter("propertyA ne \"" + DATE_STR + "\"");
        PreparedStatement statement = fixture.prepareStatement(mockConnection, "select x from table");
        assertNotNull(statement);

        verify(mockConnection).prepareStatement("select x from table WHERE a <> ?");
        verify(statement).setDate(1, new java.sql.Date(CALENDAR_VAL.toEpochMilli()));
    }

    @Test
    public void testLessThan() throws Exception {
        fixture.setFilter("propertyA lt \"" + DATE_STR + "\"");
        PreparedStatement statement = fixture.prepareStatement(mockConnection, "select x from table");
        assertNotNull(statement);

        verify(mockConnection).prepareStatement("select x from table WHERE a < ?");
        verify(statement).setDate(1, new java.sql.Date(CALENDAR_VAL.toEpochMilli()));
    }

    @Test
    public void testLessThanEqual() throws Exception {
        fixture.setFilter("propertyA le \"" + DATE_STR + "\"");
        PreparedStatement statement = fixture.prepareStatement(mockConnection, "select x from table");
        assertNotNull(statement);

        verify(mockConnection).prepareStatement("select x from table WHERE a <= ?");
        verify(statement).setDate(1, new java.sql.Date(CALENDAR_VAL.toEpochMilli()));
    }

    @Test
    public void testGreaterThanEqual() throws Exception {
        fixture.setFilter("propertyA ge \"" + DATE_STR + "\"");
        PreparedStatement statement = fixture.prepareStatement(mockConnection, "select x from table");
        assertNotNull(statement);

        verify(mockConnection).prepareStatement("select x from table WHERE a >= ?");
        verify(statement).setDate(1, new java.sql.Date(CALENDAR_VAL.toEpochMilli()));
    }

    @Test
    public void testGreaterThan() throws Exception {
        fixture.setFilter("propertyA gt \"" + DATE_STR + "\"");
        PreparedStatement statement = fixture.prepareStatement(mockConnection, "select x from table");
        assertNotNull(statement);

        verify(mockConnection).prepareStatement("select x from table WHERE a > ?");
        verify(statement).setDate(1, new java.sql.Date(CALENDAR_VAL.toEpochMilli()));
    }

    public interface TestDataClass {
        @FilterProperty(colname = "a")
        java.util.Date getPropertyA();

        @FilterProperty(colname = "b")
        java.sql.Date getPropertyB();
    }
}
