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

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 *
 * @author <a href="mailto:watson.phill@gmail.com">Phill Watson</a>
 * @since 1.0.0
 */
public class QueryConstraintsBigDecTest {
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
    public void testNumberFormatException() throws Exception {
        fixture.setFilter("propertyA EQ 'abc'");

        try {
            fixture.prepareStatement(mockConnection, "select x from table");
            fail("Expected NumberFormatException");
        } catch (NumberFormatException expected) {
        }
    }

    @Test
    public void testPositive() throws Exception {
        fixture.setFilter("propertyA eq 9999999.99999");
        PreparedStatement statement = fixture.prepareStatement(mockConnection, "select x from table");
        assertNotNull(statement);

        verify(mockConnection).prepareStatement("select x from table WHERE a = ?");
        verify(statement).setBigDecimal(1, new BigDecimal("9999999.99999"));
    }

    @Test
    public void testNegative() throws Exception {
        fixture.setFilter("propertyA eq -9999999.99999");
        PreparedStatement statement = fixture.prepareStatement(mockConnection, "select x from table");
        assertNotNull(statement);

        verify(mockConnection).prepareStatement("select x from table WHERE a = ?");
        verify(statement).setBigDecimal(1, new BigDecimal("-9999999.99999"));
    }

    @Test
    public void testIsNull() throws Exception {
        fixture.setFilter("isnull(propertyA)");
        PreparedStatement statement = fixture.prepareStatement(mockConnection, "select x from table");
        assertNotNull(statement);

        verify(mockConnection).prepareStatement("select x from table WHERE a is null");
        verify(statement, never()).setBigDecimal(anyInt(), any(BigDecimal.class));
        verifyNoMoreInteractions(statement);
    }

    @Test
    public void testNotNull() throws Exception {
        fixture.setFilter("notnull(propertyA)");
        PreparedStatement statement = fixture.prepareStatement(mockConnection, "select x from table");
        assertNotNull(statement);

        verify(mockConnection).prepareStatement("select x from table WHERE a is not null");
        verify(statement, never()).setBigDecimal(anyInt(), any(BigDecimal.class));
        verifyNoMoreInteractions(statement);
    }

    @Test
    public void testNotEq() throws Exception {
        fixture.setFilter("propertyA ne 9999999.99999");
        PreparedStatement statement = fixture.prepareStatement(mockConnection, "select x from table");
        assertNotNull(statement);

        verify(mockConnection).prepareStatement("select x from table WHERE a <> ?");
        verify(statement).setBigDecimal(1, new BigDecimal("9999999.99999"));
    }

    @Test
    public void testLessThan() throws Exception {
        fixture.setFilter("propertyA lt 9999999.99999");
        PreparedStatement statement = fixture.prepareStatement(mockConnection, "select x from table");
        assertNotNull(statement);

        verify(mockConnection).prepareStatement("select x from table WHERE a < ?");
        verify(statement).setBigDecimal(1, new BigDecimal("9999999.99999"));
    }

    @Test
    public void testLessThanEqual() throws Exception {
        fixture.setFilter("propertyA le 9999999.99999");
        PreparedStatement statement = fixture.prepareStatement(mockConnection, "select x from table");
        assertNotNull(statement);

        verify(mockConnection).prepareStatement("select x from table WHERE a <= ?");
        verify(statement).setBigDecimal(1, new BigDecimal("9999999.99999"));
    }

    @Test
    public void testGreaterThanEqual() throws Exception {
        fixture.setFilter("propertyA ge 9999999.99999");
        PreparedStatement statement = fixture.prepareStatement(mockConnection, "select x from table");
        assertNotNull(statement);

        verify(mockConnection).prepareStatement("select x from table WHERE a >= ?");
        verify(statement).setBigDecimal(1, new BigDecimal("9999999.99999"));
    }

    @Test
    public void testGreaterThan() throws Exception {
        fixture.setFilter("propertyA gt 9999999.99999");
        PreparedStatement statement = fixture.prepareStatement(mockConnection, "select x from table");
        assertNotNull(statement);

        verify(mockConnection).prepareStatement("select x from table WHERE a > ?");
        verify(statement).setBigDecimal(1, new BigDecimal("9999999.99999"));
    }

    @Test
    public void testAnd() throws Exception {
        fixture.setFilter("propertyA gt 9999999.99999 and propertyB lt 100.22");
        PreparedStatement statement = fixture.prepareStatement(mockConnection, "select x from table");
        assertNotNull(statement);

        verify(mockConnection).prepareStatement("select x from table WHERE a > ? AND b < ?");
        verify(statement).setBigDecimal(1, new BigDecimal("9999999.99999"));
        verify(statement).setBigDecimal(2, new BigDecimal("100.22"));
    }

    @Test
    public void testOr() throws Exception {
        fixture.setFilter("propertyA gt 9999999.99999 or propertyB lt 100.22");
        PreparedStatement statement = fixture.prepareStatement(mockConnection, "select x from table");
        assertNotNull(statement);

        verify(mockConnection).prepareStatement("select x from table WHERE a > ? OR b < ?");
        verify(statement).setBigDecimal(1, new BigDecimal("9999999.99999"));
        verify(statement).setBigDecimal(2, new BigDecimal("100.22"));
    }

    @Test
    public void testNot() throws Exception {
        fixture.setFilter("not propertyA gt 9999999.99999 or propertyB lt 100.22");
        PreparedStatement statement = fixture.prepareStatement(mockConnection, "select x from table");
        assertNotNull(statement);

        verify(mockConnection).prepareStatement("select x from table WHERE NOT a > ? OR b < ?");
        verify(statement).setBigDecimal(1, new BigDecimal("9999999.99999"));
        verify(statement).setBigDecimal(2, new BigDecimal("100.22"));
    }

    public interface TestDataClass {
        @FilterProperty(colname = "a")
        BigDecimal getPropertyA();

        @FilterProperty(colname = "b")
        BigDecimal getPropertyB();
    }
}
