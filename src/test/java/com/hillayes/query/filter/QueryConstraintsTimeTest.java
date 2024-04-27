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
import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author <a href="mailto:watson.phill@gmail.com">Phill Watson</a>
 * @since 1.0.0
 */
public class QueryConstraintsTimeTest {
    private static final String DATE_STR = "21:22:30Z";

    private static final LocalTime CALENDAR_VAL = OffsetTime.parse(DATE_STR).toLocalTime();

    private Connection mockConnection;

    private QueryConstraints fixture;

    @BeforeEach
    public void initMocks() throws Exception {
        PreparedStatement mockStatement = spy(PreparedStatement.class);

        mockConnection = spy(Connection.class);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);

        fixture = new QueryConstraints(new DataClassQueryContext(TestDataClass.class));
    }

    @Test
    public void testFormatException() throws Exception {
        fixture.setFilter("propertyA EQ 'abc'");

        assertThrows(DateTimeParseException.class, () -> fixture.prepareStatement(mockConnection, "select x from table"));
    }

    @Test
    public void test() throws Exception {
        fixture.setFilter("propertyA eq \"" + DATE_STR + "\"");
        PreparedStatement statement = fixture.prepareStatement(mockConnection, "select x from table");
        assertNotNull(statement);

        verify(mockConnection).prepareStatement("select x from table WHERE a = ?");
        verify(statement).setTime(1, java.sql.Time.valueOf(CALENDAR_VAL));
    }

    @Test
    public void testIsNull() throws Exception {
        fixture.setFilter("isnull(propertyA)");
        PreparedStatement statement = fixture.prepareStatement(mockConnection, "select x from table");
        assertNotNull(statement);

        verify(mockConnection).prepareStatement("select x from table WHERE a is null");
        verify(statement, never()).setTime(anyInt(), any());
        verifyNoMoreInteractions(statement);
    }

    @Test
    public void testNotNull() throws Exception {
        fixture.setFilter("notnull(propertyA)");
        PreparedStatement statement = fixture.prepareStatement(mockConnection, "select x from table");
        assertNotNull(statement);

        verify(mockConnection).prepareStatement("select x from table WHERE a is not null");
        verify(statement, never()).setTime(anyInt(), any());
        verifyNoMoreInteractions(statement);
    }

    @Test
    public void testNotEq() throws Exception {
        fixture.setFilter("propertyA ne \"" + DATE_STR + "\"");
        PreparedStatement statement = fixture.prepareStatement(mockConnection, "select x from table");
        assertNotNull(statement);

        verify(mockConnection).prepareStatement("select x from table WHERE a <> ?");
        verify(statement).setTime(1, java.sql.Time.valueOf(CALENDAR_VAL));
    }

    @Test
    public void testLessThan() throws Exception {
        fixture.setFilter("propertyA lt \"" + DATE_STR + "\"");
        PreparedStatement statement = fixture.prepareStatement(mockConnection, "select x from table");
        assertNotNull(statement);

        verify(mockConnection).prepareStatement("select x from table WHERE a < ?");
        verify(statement).setTime(1, java.sql.Time.valueOf(CALENDAR_VAL));
    }

    @Test
    public void testLessThanEqual() throws Exception {
        fixture.setFilter("propertyA le \"" + DATE_STR + "\"");
        PreparedStatement statement = fixture.prepareStatement(mockConnection, "select x from table");
        assertNotNull(statement);

        verify(mockConnection).prepareStatement("select x from table WHERE a <= ?");
        verify(statement).setTime(1, java.sql.Time.valueOf(CALENDAR_VAL));
    }

    @Test
    public void testGreaterThanEqual() throws Exception {
        fixture.setFilter("propertyA ge \"" + DATE_STR + "\"");
        PreparedStatement statement = fixture.prepareStatement(mockConnection, "select x from table");
        assertNotNull(statement);

        verify(mockConnection).prepareStatement("select x from table WHERE a >= ?");
        verify(statement).setTime(1, java.sql.Time.valueOf(CALENDAR_VAL));
    }

    @Test
    public void testGreaterThan() throws Exception {
        fixture.setFilter("propertyA gt \"" + DATE_STR + "\"");
        PreparedStatement statement = fixture.prepareStatement(mockConnection, "select x from table");
        assertNotNull(statement);

        verify(mockConnection).prepareStatement("select x from table WHERE a > ?");
        
        verify(statement).setTime(1, java.sql.Time.valueOf(CALENDAR_VAL));
    }

    public interface TestDataClass {
        @FilterProperty(colname = "a")
        java.sql.Time getPropertyA();
    }
}
