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

import com.hillayes.query.filter.function.BiFunction;
import com.hillayes.query.filter.function.BoolFunction;
import com.hillayes.query.filter.function.UnaryFunction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
        assertEquals("%value%", BiFunction.CONTAINS.formatValue("VALUE"));
        assertEquals("value%", BiFunction.STARTSWITH.formatValue("VALUE"));
        assertEquals("%value", BiFunction.ENDSWITH.formatValue("VALUE"));
        assertEquals("VALUE", UnaryFunction.UPPER.formatValue("VALUE"));
        assertEquals("VALUE", UnaryFunction.LOWER.formatValue("VALUE"));
        assertEquals("VALUE", BoolFunction.ISNULL.formatValue("VALUE"));
        assertEquals("VALUE", BoolFunction.NOTNULL.formatValue("VALUE"));
    }

    @Test
    public void testAppendTo()
    {
        StringBuilder result = new StringBuilder();

        result.setLength(0);
        BiFunction.CONTAINS.appendTo(result, "col");
        assertEquals("col like ?", result.toString());

        result.setLength(0);
        BiFunction.STARTSWITH.appendTo(result, "col");
        assertEquals("col like ?", result.toString());

        result.setLength(0);
        BiFunction.ENDSWITH.appendTo(result, "col");
        assertEquals("col like ?", result.toString());

        result.setLength(0);
        UnaryFunction.LOWER.appendTo(result, "col");
        assertEquals("LOWER(col)", result.toString());

        result.setLength(0);
        UnaryFunction.UPPER.appendTo(result, "col");
        assertEquals("UPPER(col)", result.toString());

        result.setLength(0);
        BoolFunction.ISNULL.appendTo(result, "col");
        assertEquals("col is null", result.toString());

        result.setLength(0);
        BoolFunction.NOTNULL.appendTo(result, "col");
        assertEquals("col is not null", result.toString());
    }
}
