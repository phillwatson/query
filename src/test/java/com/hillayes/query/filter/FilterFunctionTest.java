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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 *
 * @author <a href="mailto:watson.phill@gmail.com">Phill Watson</a>
 * @since 1.0.0
 */
public class FilterFunctionTest
{
    @Test
    public void testFormatValue()
    {
        assertEquals("%value%", FilterFunction.CONTAINS.formatValue("VALUE"));
        assertEquals("value%", FilterFunction.STARTSWITH.formatValue("VALUE"));
        assertEquals("%value", FilterFunction.ENDSWITH.formatValue("VALUE"));
        assertEquals("VALUE", FilterFunction.UPPER.formatValue("VALUE"));
        assertEquals("VALUE", FilterFunction.LOWER.formatValue("VALUE"));
        assertEquals("VALUE", FilterFunction.ISNULL.formatValue("VALUE"));
        assertEquals("VALUE", FilterFunction.NOTNULL.formatValue("VALUE"));
    }

    @Test
    public void testTakesValue()
    {
        assertTrue(FilterFunction.CONTAINS.takesValue());
        assertTrue(FilterFunction.STARTSWITH.takesValue());
        assertTrue(FilterFunction.ENDSWITH.takesValue());
        assertFalse(FilterFunction.UPPER.takesValue());
        assertFalse(FilterFunction.LOWER.takesValue());
        assertFalse(FilterFunction.ISNULL.takesValue());
        assertFalse(FilterFunction.NOTNULL.takesValue());
    }

    @Test
    public void testAppendTo()
    {
        StringBuilder result = new StringBuilder();

        result.setLength(0);
        FilterFunction.CONTAINS.appendTo(result, "col");
        assertEquals("col like ?", result.toString());

        result.setLength(0);
        FilterFunction.STARTSWITH.appendTo(result, "col");
        assertEquals("col like ?", result.toString());

        result.setLength(0);
        FilterFunction.ENDSWITH.appendTo(result, "col");
        assertEquals("col like ?", result.toString());

        result.setLength(0);
        FilterFunction.LOWER.appendTo(result, "col");
        assertEquals("LOWER(col)", result.toString());

        result.setLength(0);
        FilterFunction.UPPER.appendTo(result, "col");
        assertEquals("UPPER(col)", result.toString());

        result.setLength(0);
        FilterFunction.ISNULL.appendTo(result, "col");
        assertEquals("col is null", result.toString());

        result.setLength(0);
        FilterFunction.NOTNULL.appendTo(result, "col");
        assertEquals("col is not null", result.toString());
    }
}
