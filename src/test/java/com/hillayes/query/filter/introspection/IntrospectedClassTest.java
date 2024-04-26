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
package com.hillayes.query.filter.introspection;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author <a href="mailto:watson.phill@gmail.com">Phill Watson</a>
 * @since 1.0.0
 */
public class IntrospectedClassTest
{
    @Test
    public void testNullDataClass() throws Exception
    {
        try
        {
            IntrospectedClass.introspect(null);
            fail("Expected NullPointerException");
        }
        catch (NullPointerException expected)
        {
            assertEquals("A class must be provided for introspection.", expected.getMessage());
        }
    }

    @Test
    public void testCache() throws Exception
    {
        IntrospectedClass introspection = IntrospectedClass.introspect(TestClass.class);

        assertSame(introspection, IntrospectedClass.introspect(TestClass.class));
    }

    @Test
    public void testCompoundProperties() throws Exception {
        IntrospectedClass introspection = IntrospectedClass.introspect(TestClass.class);
        assertNotNull(introspection);

        assertNotNull(introspection.getProperty("valueA"));
        assertNotNull(introspection.getProperty("valueB"));
        assertNotNull(introspection.getProperty("compoundC.valueX"));
        assertNotNull(introspection.getProperty("compoundC.valueY"));
        assertNotNull(introspection.getProperty("propertyD.valueX"));
        assertNotNull(introspection.getProperty("propertyD.valueY"));
    }

    interface TestClass
    {
        @FilterProperty
        public boolean isValueA();

        @FilterProperty
        public int getValueB();

        @FilterProperty
        public CompoundProperty getCompoundC();

        @FilterProperty(name = "propertyD")
        public CompoundProperty getCompoundD();
    }

    class CompoundProperty {
        @FilterProperty
        public boolean isValueX() {
            return true;
        }

        @FilterProperty
        public int getValueY() {
            return 1;
        }
    }
}
