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

import com.hillayes.query.filter.exceptions.InvalidPropertyRefException;
import com.hillayes.query.filter.parser.ParseException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 *
 * @author <a href="mailto:watson.phill@gmail.com">Phill Watson</a>
 * @since 1.0.0
 */
public class PredicateTest {
    @Test
    public void testFunction() throws Exception {
        Predicate fixture = new Predicate(null);

        assertNull(fixture.getFunction());
        fixture.setFunction(FilterFunction.CONTAINS);
        assertEquals(FilterFunction.CONTAINS, fixture.getFunction());
    }

    @Test
    public void testName() throws Exception {
        QueryContext mockContext = Mockito.mock(QueryContext.class);
        when(mockContext.getPropertyFor(anyString())).thenReturn(Mockito.mock(QueryProperty.class));

        Predicate fixture = new Predicate(mockContext);

        assertNull(fixture.getName());
        fixture.setName("property");
        assertEquals("property", fixture.getName());
    }

    @Test
    public void testNameUnknown() throws Exception {
        QueryContext mockContext = Mockito.mock(QueryContext.class);
        when(mockContext.getPropertyFor(anyString())).thenReturn(null);

        Predicate fixture = new Predicate(mockContext);
        assertThrows(InvalidPropertyRefException.class, () -> fixture.setName("property"));
    }

    @Test
    public void testOperator() throws Exception {
        Predicate fixture = new Predicate(null);

        assertNull(fixture.getOperator());
        fixture.setOperator(Operator.GE);
        assertEquals(Operator.GE, fixture.getOperator());
    }

    @Test
    public void testString() throws Exception {
        Predicate fixture = new Predicate(null);

        assertNull(fixture.getValue());
        fixture.setString("value1");
        assertEquals("value1", fixture.getValue());
        assertFalse(fixture.isNumeric());
    }

    @Test
    public void testStringSingleQuote() throws Exception {
        Predicate fixture = new Predicate(null);

        assertNull(fixture.getValue());
        fixture.setString("'value2'");
        assertEquals("value2", fixture.getValue());
        assertFalse(fixture.isNumeric());
    }

    @Test
    public void testStringDoubleQuote() throws Exception {
        Predicate fixture = new Predicate(null);

        assertNull(fixture.getValue());
        fixture.setString("\"value3\"");
        assertEquals("value3", fixture.getValue());
        assertFalse(fixture.isNumeric());
    }

    @Test
    public void testNumber() throws Exception {
        Predicate fixture = new Predicate(null);

        assertNull(fixture.getValue());
        fixture.setNumber("999");
        assertEquals("999", fixture.getValue());
        assertTrue(fixture.isNumeric());
    }

    @Test
    public void testIdentifier() throws Exception {
        Predicate fixture = new Predicate(null);

        assertNull(fixture.getValue());
        fixture.setIdentifier("Property");
        assertEquals("Property", fixture.getValue());
        assertFalse(fixture.isNumeric());
    }
}
