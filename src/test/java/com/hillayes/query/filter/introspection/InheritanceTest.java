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

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 *
 * @author <a href="mailto:watson.phill@gmail.com">Phill Watson</a>
 * @since 1.0.0
 */
public class InheritanceTest
{
    @Test
    public void testClassInheritance() throws Exception
    {
        Introspection introspection = PropertyIntrospector.introspect(ClassC.class);

        assertNotNull(introspection);

        // all class methods are present
        assertNotNull(introspection.getProperty("propertyA"));
        assertNotNull(introspection.getProperty("propertyB"));
        assertNotNull(introspection.getProperty("propertyC"));
    }

    @Test
    public void testInterfaceInheritance() throws Exception
    {
        Introspection introspection = PropertyIntrospector.introspect(InterfaceC.class);

        assertNotNull(introspection);

        // InterfaceB was only processed once
        assertEquals(3, introspection.getProperties().size());

        // all interface methods are present
        assertNotNull(introspection.getProperty("propertyA"));
        assertNotNull(introspection.getProperty("propertyB"));
        assertNotNull(introspection.getProperty("propertyC"));
    }

    @Test
    public void testClassAndInterfaceInheritance() throws Exception
    {
        Introspection introspection = PropertyIntrospector.introspect(ClassD.class);

        assertNotNull(introspection);

        // Interfaces were only processed once
        assertEquals(4, introspection.getProperties().size());

        // all class and interface methods are present
        assertNotNull(introspection.getProperty("propertyA"));
        assertNotNull(introspection.getProperty("propertyB"));
        assertNotNull(introspection.getProperty("propertyC"));
        assertNotNull(introspection.getProperty("propertyD"));
    }

    class ClassA
    {
        @FilterProperty
        public String getPropertyA()
        {
            return null;
        }
    }

    class ClassB extends ClassA
    {
        @FilterProperty
        public String getPropertyB()
        {
            return null;
        }
    }

    class ClassC extends ClassB
    {
        @FilterProperty
        public String getPropertyC()
        {
            return null;
        }
    }

    interface InterfaceA
    {
        @FilterProperty
        public String getPropertyA();
    }

    interface InterfaceB extends InterfaceA
    {
        @FilterProperty
        public String getPropertyB();
    }

    interface InterfaceC extends InterfaceA, InterfaceB
    {
        @FilterProperty
        public String getPropertyC();
    }

    class ClassD extends ClassC implements InterfaceC
    {
        @FilterProperty
        public Calendar getPropertyD()
        {
            return null;
        }
    }
}
