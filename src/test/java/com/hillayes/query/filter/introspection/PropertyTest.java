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
package com.hillayes.query.filter.introspection;

import com.hillayes.query.filter.Property;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.time.Instant;
import java.time.LocalTime;
import java.time.OffsetTime;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

/**
 * @author <a href="mailto:watson.phill@gmail.com">Phill Watson</a>
 * @since 1.0.0
 */
public class PropertyTest {
    @Test
    public void testToTypeString() throws Exception {
        PreparedStatement statement;

        Property fixture = new Property("name", "col_name", String.class);

        statement = spy(PreparedStatement.class);
        fixture.applyTo(statement, 1, "String Value");
        verify(statement).setString(1, "String Value");
    }

    @Test
    public void testToType_boolean() throws Exception {
        PreparedStatement statement;

        Property fixture = new Property("name", "col_name", boolean.class);

        statement = spy(PreparedStatement.class);
        fixture.applyTo(statement, 1, "True");
        verify(statement).setBoolean(1, true);

        statement = spy(PreparedStatement.class);
        fixture.applyTo(statement, 1, "False");
        verify(statement).setBoolean(1, false);
    }

    @Test
    public void testToTypeBoolean() throws Exception {
        PreparedStatement statement;

        Property fixture = new Property("name", "col_name", Boolean.class);

        statement = spy(PreparedStatement.class);
        fixture.applyTo(statement, 1, "True");
        verify(statement).setBoolean(1, true);

        statement = spy(PreparedStatement.class);
        fixture.applyTo(statement, 1, "False");
        verify(statement).setBoolean(1, false);
    }

    @Test
    public void testToTypeByte() throws Exception {
        PreparedStatement statement;

        Property fixture = new Property("name", "col_name", Byte.class);

        statement = spy(PreparedStatement.class);
        fixture.applyTo(statement, 1, "123");
        verify(statement).setByte(1, (byte) 123);
    }

    @Test
    public void testToType_byte() throws Exception {
        PreparedStatement statement;

        Property fixture = new Property("name", "col_name", byte.class);

        statement = spy(PreparedStatement.class);
        fixture.applyTo(statement, 1, "123");
        verify(statement).setByte(1, (byte) 123);
    }

    @Test
    public void testToTypeInteger() throws Exception {
        PreparedStatement statement;

        Property fixture = new Property("name", "col_name", Integer.class);

        statement = spy(PreparedStatement.class);
        fixture.applyTo(statement, 1, "999");
        verify(statement).setInt(1, 999);
    }

    @Test
    public void testToTypeInt() throws Exception {
        PreparedStatement statement;

        Property fixture = new Property("name", "col_name", int.class);

        statement = spy(PreparedStatement.class);
        fixture.applyTo(statement, 1, "999");
        verify(statement).setInt(1, 999);
    }

    @Test
    public void testToTypeLong() throws Exception {
        PreparedStatement statement;

        Property fixture = new Property("name", "col_name", Long.class);

        statement = spy(PreparedStatement.class);
        fixture.applyTo(statement, 1, "999");
        verify(statement).setLong(1, 999);
    }

    @Test
    public void testToType_long() throws Exception {
        PreparedStatement statement;

        Property fixture = new Property("name", "col_name", long.class);

        statement = spy(PreparedStatement.class);
        fixture.applyTo(statement, 1, "999");
        verify(statement).setLong(1, 999);
    }

    @Test
    public void testToTypeBigInteger() throws Exception {
        PreparedStatement statement;

        Property fixture = new Property("name", "col_name", BigInteger.class);

        statement = spy(PreparedStatement.class);
        fixture.applyTo(statement, 1, "999");
        verify(statement).setLong(1, 999);
    }

    @Test
    public void testToTypeBigDecimal() throws Exception {
        PreparedStatement statement;

        Property fixture = new Property("name", "col_name", BigDecimal.class);

        statement = spy(PreparedStatement.class);
        BigDecimal value = new BigDecimal("999.99");
        fixture.applyTo(statement, 1, "999.99");
        verify(statement).setBigDecimal(1, value);
    }

    @Test
    public void testToTypeDouble() throws Exception {
        PreparedStatement statement;

        Property fixture = new Property("name", "col_name", Double.class);

        statement = spy(PreparedStatement.class);
        fixture.applyTo(statement, 1, "999.99");
        verify(statement).setDouble(1, 999.99);
    }

    @Test
    public void testToType_double() throws Exception {
        PreparedStatement statement;

        Property fixture = new Property("name", "col_name", double.class);

        statement = spy(PreparedStatement.class);
        fixture.applyTo(statement, 1, "999.99");
        verify(statement).setDouble(1, 999.99);
    }

    @Test
    public void testToTypeFloat() throws Exception {
        PreparedStatement statement;

        Property fixture = new Property("name", "col_name", Float.class);

        statement = spy(PreparedStatement.class);
        fixture.applyTo(statement, 1, "999.99");
        verify(statement).setFloat(1, 999.99f);
    }

    @Test
    public void testToType_float() throws Exception {
        PreparedStatement statement;

        Property fixture = new Property("name", "col_name", float.class);

        statement = spy(PreparedStatement.class);
        fixture.applyTo(statement, 1, "999.99");
        verify(statement).setFloat(1, 999.99f);
    }

    @Test
    public void testToTypeCalendar() throws Exception {
        PreparedStatement statement;

        Property fixture = new Property("name", "col_name", Calendar.class);

        statement = spy(PreparedStatement.class);
        fixture.applyTo(statement, 1, "2018-10-01T20:30:40.000Z");
        Instant cal = Instant.parse("2018-10-01T20:30:40.000Z");
        verify(statement).setDate(1, new java.sql.Date(cal.toEpochMilli()));
    }

    @Test
    public void testToTypeDate() throws Exception {
        PreparedStatement statement;

        Property fixture = new Property("name", "col_name", Date.class);

        statement = spy(PreparedStatement.class);
        fixture.applyTo(statement, 1, "2018-10-01T20:30:40.000Z");
        Instant cal = Instant.parse("2018-10-01T20:30:40.000Z");
        verify(statement).setDate(1, new java.sql.Date(cal.toEpochMilli()));
    }

    @Test
    public void testToTypeSqlDate() throws Exception {
        PreparedStatement statement;

        Property fixture = new Property("name", "col_name", java.sql.Date.class);

        statement = spy(PreparedStatement.class);
        fixture.applyTo(statement, 1, "2018-10-01T20:30:40.000Z");
        Instant cal = Instant.parse("2018-10-01T20:30:40.000Z");
        verify(statement).setDate(1, new java.sql.Date(cal.toEpochMilli()));
    }

    @Test
    public void testToTypeTime() throws Exception {
        PreparedStatement statement;

        Property fixture = new Property("name", "col_name", java.sql.Time.class);

        statement = spy(PreparedStatement.class);
        fixture.applyTo(statement, 1, "20:30:40.000+01:00");
        LocalTime cal = OffsetTime.parse("20:30:40.000Z").toLocalTime();
        verify(statement).setTime(1, java.sql.Time.valueOf(cal));

        statement = spy(PreparedStatement.class);
        fixture.applyTo(statement, 1, "20:30:40.000Z");
        cal = OffsetTime.parse("20:30:40.000Z").toLocalTime();
        verify(statement).setTime(1, java.sql.Time.valueOf(cal));
    }

    @Test
    public void testToTypeTimestamp() throws Exception {
        PreparedStatement statement;

        Property fixture = new Property("name", "col_name", java.sql.Timestamp.class);

        statement = spy(PreparedStatement.class);
        fixture.applyTo(statement, 1, "2018-10-01T20:30:40.000Z");
        Instant cal = Instant.parse("2018-10-01T20:30:40.000Z");
        verify(statement).setTimestamp(1, java.sql.Timestamp.from(cal));
    }

    @Test
    public void testToTypeUUID() throws Exception {
        PreparedStatement statement;

        Property fixture = new Property("name", "col_name", UUID.class);

        statement = spy(PreparedStatement.class);
        UUID value = UUID.randomUUID();
        fixture.applyTo(statement, 1, value.toString());
        verify(statement).setObject(1, value);
    }
}
