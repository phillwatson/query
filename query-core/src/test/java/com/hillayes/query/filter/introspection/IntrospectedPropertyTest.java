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

import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.time.Instant;
import java.time.LocalTime;
import java.time.OffsetTime;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.UUID;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

/**
 * @author <a href="mailto:watson.phill@gmail.com">Phill Watson</a>
 * @since 1.0.0
 */
public class IntrospectedPropertyTest {
    @Test
    public void testToTypeString() throws Exception {
        FilterProperty annotation = new MockFilterProperty("name", "col_name", String.class);
        IntrospectedProperty fixture = new IntrospectedProperty(annotation, "col_name", String.class, null);

        PreparedStatement statement = spy(PreparedStatement.class);
        fixture.applyTo(statement, 1, "String Value");
        verify(statement).setString(1, "String Value");
    }

    @Test
    public void testToType_boolean() throws Exception {
        FilterProperty annotation = new MockFilterProperty("name", "col_name", boolean.class);
        IntrospectedProperty fixture = new IntrospectedProperty(annotation, "col_name", boolean.class, null);

        PreparedStatement statement = spy(PreparedStatement.class);
        fixture.applyTo(statement, 1, "True");
        verify(statement).setBoolean(1, true);

        statement = spy(PreparedStatement.class);
        fixture.applyTo(statement, 1, "False");
        verify(statement).setBoolean(1, false);
    }

    @Test
    public void testToTypeBoolean() throws Exception {
        FilterProperty annotation = new MockFilterProperty("name", "col_name", Boolean.class);
        IntrospectedProperty fixture = new IntrospectedProperty(annotation, "col_name", Boolean.class, null);

        PreparedStatement statement = spy(PreparedStatement.class);
        fixture.applyTo(statement, 1, "True");
        verify(statement).setBoolean(1, true);

        statement = spy(PreparedStatement.class);
        fixture.applyTo(statement, 1, "False");
        verify(statement).setBoolean(1, false);
    }

    @Test
    public void testToTypeByte() throws Exception {
        FilterProperty annotation = new MockFilterProperty("name", "col_name", Byte.class);
        IntrospectedProperty fixture = new IntrospectedProperty(annotation, "col_name", Byte.class, null);

        PreparedStatement statement = spy(PreparedStatement.class);
        fixture.applyTo(statement, 1, "123");
        verify(statement).setByte(1, (byte) 123);
    }

    @Test
    public void testToType_byte() throws Exception {
        FilterProperty annotation = new MockFilterProperty("name", "col_name", byte.class);
        IntrospectedProperty fixture = new IntrospectedProperty(annotation, "col_name", byte.class, null);

        PreparedStatement statement = spy(PreparedStatement.class);
        fixture.applyTo(statement, 1, "123");
        verify(statement).setByte(1, (byte) 123);
    }

    @Test
    public void testToTypeInteger() throws Exception {
        FilterProperty annotation = new MockFilterProperty("name", "col_name", Integer.class);
        IntrospectedProperty fixture = new IntrospectedProperty(annotation, "col_name", Integer.class, null);

        PreparedStatement statement = spy(PreparedStatement.class);
        fixture.applyTo(statement, 1, "999");
        verify(statement).setInt(1, 999);
    }

    @Test
    public void testToTypeInt() throws Exception {
        FilterProperty annotation = new MockFilterProperty("name", "col_name", int.class);
        IntrospectedProperty fixture = new IntrospectedProperty(annotation, "col_name", int.class, null);

        PreparedStatement statement = spy(PreparedStatement.class);
        fixture.applyTo(statement, 1, "999");
        verify(statement).setInt(1, 999);
    }

    @Test
    public void testToTypeLong() throws Exception {
        FilterProperty annotation = new MockFilterProperty("name", "col_name", Long.class);
        IntrospectedProperty fixture = new IntrospectedProperty(annotation, "col_name", Long.class, null);

        PreparedStatement statement = spy(PreparedStatement.class);
        fixture.applyTo(statement, 1, "999");
        verify(statement).setLong(1, 999);
    }

    @Test
    public void testToType_long() throws Exception {
        FilterProperty annotation = new MockFilterProperty("name", "col_name", long.class);
        IntrospectedProperty fixture = new IntrospectedProperty(annotation, "col_name", long.class, null);

        PreparedStatement statement = spy(PreparedStatement.class);
        fixture.applyTo(statement, 1, "999");
        verify(statement).setLong(1, 999);
    }

    @Test
    public void testToTypeBigInteger() throws Exception {
        FilterProperty annotation = new MockFilterProperty("name", "col_name", BigInteger.class);
        IntrospectedProperty fixture = new IntrospectedProperty(annotation, "col_name", BigInteger.class, null);

        PreparedStatement statement = spy(PreparedStatement.class);
        fixture.applyTo(statement, 1, "999");
        verify(statement).setLong(1, 999);
    }

    @Test
    public void testToTypeBigDecimal() throws Exception {
        FilterProperty annotation = new MockFilterProperty("name", "col_name", BigDecimal.class);
        IntrospectedProperty fixture = new IntrospectedProperty(annotation, "col_name", BigDecimal.class, null);

        PreparedStatement statement = spy(PreparedStatement.class);
        BigDecimal value = new BigDecimal("999.99");
        fixture.applyTo(statement, 1, "999.99");
        verify(statement).setBigDecimal(1, value);
    }

    @Test
    public void testToTypeDouble() throws Exception {
        FilterProperty annotation = new MockFilterProperty("name", "col_name", Double.class);
        IntrospectedProperty fixture = new IntrospectedProperty(annotation, "col_name", Double.class, null);

        PreparedStatement statement = spy(PreparedStatement.class);
        fixture.applyTo(statement, 1, "999.99");
        verify(statement).setDouble(1, 999.99);
    }

    @Test
    public void testToType_double() throws Exception {
        FilterProperty annotation = new MockFilterProperty("name", "col_name", double.class);
        IntrospectedProperty fixture = new IntrospectedProperty(annotation, "col_name", double.class, null);

        PreparedStatement statement = spy(PreparedStatement.class);
        fixture.applyTo(statement, 1, "999.99");
        verify(statement).setDouble(1, 999.99);
    }

    @Test
    public void testToTypeFloat() throws Exception {
        FilterProperty annotation = new MockFilterProperty("name", "col_name", Float.class);
        IntrospectedProperty fixture = new IntrospectedProperty(annotation, "col_name", Float.class, null);

        PreparedStatement statement = spy(PreparedStatement.class);
        fixture.applyTo(statement, 1, "999.99");
        verify(statement).setFloat(1, 999.99f);
    }

    @Test
    public void testToType_float() throws Exception {
        FilterProperty annotation = new MockFilterProperty("name", "col_name", float.class);
        IntrospectedProperty fixture = new IntrospectedProperty(annotation, "col_name", float.class, null);

        PreparedStatement statement = spy(PreparedStatement.class);
        fixture.applyTo(statement, 1, "999.99");
        verify(statement).setFloat(1, 999.99f);
    }

    @Test
    public void testToTypeCalendar() throws Exception {
        FilterProperty annotation = new MockFilterProperty("name", "col_name", Calendar.class);
        IntrospectedProperty fixture = new IntrospectedProperty(annotation, "col_name", Calendar.class, null);

        PreparedStatement statement = spy(PreparedStatement.class);
        fixture.applyTo(statement, 1, "2018-10-01T20:30:40.000Z");
        Instant cal = Instant.parse("2018-10-01T20:30:40.000Z");
        verify(statement).setDate(1, new java.sql.Date(cal.toEpochMilli()));
    }

    @Test
    public void testToTypeDate() throws Exception {
        FilterProperty annotation = new MockFilterProperty("name", "col_name", Date.class);
        IntrospectedProperty fixture = new IntrospectedProperty(annotation, "col_name", Date.class, null);

        PreparedStatement statement = spy(PreparedStatement.class);
        fixture.applyTo(statement, 1, "2018-10-01T20:30:40.000Z");
        Instant cal = Instant.parse("2018-10-01T20:30:40.000Z");
        verify(statement).setDate(1, new java.sql.Date(cal.toEpochMilli()));
    }

    @Test
    public void testToTypeSqlDate() throws Exception {
        FilterProperty annotation = new MockFilterProperty("name", "col_name", java.sql.Date.class);
        IntrospectedProperty fixture = new IntrospectedProperty(annotation, "col_name", java.sql.Date.class, null);

        PreparedStatement statement = spy(PreparedStatement.class);
        fixture.applyTo(statement, 1, "2018-10-01T20:30:40.000Z");
        Instant cal = Instant.parse("2018-10-01T20:30:40.000Z");
        verify(statement).setDate(1, new java.sql.Date(cal.toEpochMilli()));
    }

    @Test
    public void testToTypeTime() throws Exception {
        FilterProperty annotation = new MockFilterProperty("name", "col_name", java.sql.Time.class);
        IntrospectedProperty fixture = new IntrospectedProperty(annotation, "col_name", java.sql.Time.class, null);

        PreparedStatement statement = spy(PreparedStatement.class);
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
        FilterProperty annotation = new MockFilterProperty("name", "col_name", java.sql.Timestamp.class);
        IntrospectedProperty fixture = new IntrospectedProperty(annotation, "col_name", java.sql.Timestamp.class, null);

        PreparedStatement statement = spy(PreparedStatement.class);
        fixture.applyTo(statement, 1, "2018-10-01T20:30:40.000Z");
        Instant cal = Instant.parse("2018-10-01T20:30:40.000Z");
        verify(statement).setTimestamp(1, java.sql.Timestamp.from(cal));
    }

    @Test
    public void testToTypeUUID() throws Exception {
        FilterProperty annotation = new MockFilterProperty("name", "col_name", UUID.class);
        IntrospectedProperty fixture = new IntrospectedProperty(annotation, "col_name", UUID.class, null);

        PreparedStatement statement = spy(PreparedStatement.class);
        UUID value = UUID.randomUUID();
        fixture.applyTo(statement, 1, value.toString());
        verify(statement).setObject(1, value);
    }

    @Test
    public void testToTypeCurrency() throws Exception {
        FilterProperty annotation = new MockFilterProperty("name", "col_name", Currency.class);
        IntrospectedProperty fixture = new IntrospectedProperty(annotation, "col_name", Currency.class, null);

        PreparedStatement statement = spy(PreparedStatement.class);
        fixture.applyTo(statement, 1, "GBP");
        verify(statement).setString(1, "GBP");
    }

    private class MockFilterProperty implements FilterProperty {
        private String name;
        private String colname;
        private Class<?> type;

        public MockFilterProperty(String name, String colname, Class<?> type) {
            this.name = name;
            this.colname = colname;
            this.type = type;
        }

        @Override
        public String name() {
            return name;
        }

        @Override
        public String colname() {
            return colname;
        }

        @Override
        public Class<?> type() {
            return type;
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return FilterProperty.class;
        }
    }
}
