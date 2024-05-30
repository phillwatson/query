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

import com.hillayes.query.filter.QueryProperty;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.util.Calendar;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author <a href="mailto:watson.phill@gmail.com">Phill Watson</a>
 * @since 1.0.0
 */
public class DataTypeTest
{
    @Test
    public void testSuportedTypes() throws Exception
    {
        IntrospectedClass introspection = IntrospectedClass.introspect(SupportedTypes.class);

        assertNotNull(introspection);

        assertEquals(17, introspection.getProperties().size());

        // all methods present
        assertNotNull(introspection.getProperty("propertyA"));
        assertNotNull(introspection.getProperty("propertyB"));
        assertNotNull(introspection.getProperty("propertyC"));
        assertNotNull(introspection.getProperty("propertyD"));
        assertNotNull(introspection.getProperty("propertyE"));
        assertNotNull(introspection.getProperty("propertyF"));
        assertNotNull(introspection.getProperty("propertyG"));
        assertNotNull(introspection.getProperty("propertyH"));
        assertNotNull(introspection.getProperty("propertyI"));
        assertNotNull(introspection.getProperty("propertyJ"));
        assertNotNull(introspection.getProperty("propertyK"));
        assertNotNull(introspection.getProperty("propertyL"));
        assertNotNull(introspection.getProperty("propertyM"));
        assertNotNull(introspection.getProperty("propertyN"));
        assertNotNull(introspection.getProperty("propertyO"));
        assertNotNull(introspection.getProperty("propertyP"));
        assertNotNull(introspection.getProperty("propertyQ"));
    }

    @Test
    public void testVoidMethods() throws Exception
    {
        // void methods aren't included
        IntrospectedClass introspection = IntrospectedClass.introspect(VoidType.class);
        assertNotNull(introspection);
        assertTrue(introspection.getProperties().isEmpty());
    }

    @Test
    public void testBooleanMethods() throws Exception
    {
        // "is" dropped from boolean method name
        IntrospectedClass introspection = IntrospectedClass.introspect(BooleanTestClass.class);
        assertNotNull(introspection);

        assertNotNull(introspection.getProperty("valueA"));
        assertNotNull(introspection.getProperty("boolB"));
        assertNotNull(introspection.getProperty("valueC"));
        assertNotNull(introspection.getProperty("boolD"));
        assertNotNull(introspection.getProperty("valueE"));
        assertNotNull(introspection.getProperty("boolF"));
    }

    @Test
    public void testNonAnnotatedProperties() throws Exception
    {
        IntrospectedClass introspection = IntrospectedClass.introspect(UnannotatedTestClass.class);
        assertNotNull(introspection);

        assertTrue(introspection.getProperties().isEmpty());
    }

    @Test
    public void testStaticProperties() throws Exception
    {
        IntrospectedClass introspection = IntrospectedClass.introspect(StaticTestClass.class);
        assertNotNull(introspection);

        assertEquals(1, introspection.getProperties().size());
        assertNotNull(introspection.getProperty("propertyB"));
    }

    @Test
    public void testProtectedProperties() throws Exception
    {
        IntrospectedClass introspection = IntrospectedClass.introspect(ProtectedTestClass.class);
        assertNotNull(introspection);

        assertEquals(1, introspection.getProperties().size());
        assertNotNull(introspection.getProperty("propertyB"));
    }

    @Test
    public void testWriteOnlyProperties() throws Exception
    {
        IntrospectedClass introspection = IntrospectedClass.introspect(WriteOnlyTestClass.class);
        assertNotNull(introspection);

        assertEquals(1, introspection.getProperties().size());
        assertNotNull(introspection.getProperty("propertyA"));
    }

    @Test
    public void testAnnotationOverideProperties() throws Exception
    {
        IntrospectedClass introspection = IntrospectedClass.introspect(OverrideTestClass.class);
        assertNotNull(introspection);

        assertEquals(2, introspection.getProperties().size());

        QueryProperty propertyA = introspection.getProperty("nameA");
        assertNotNull(propertyA);
        assertEquals("nameA", propertyA.getName());
        assertEquals("colA", propertyA.getColName());
        assertEquals(Date.class, propertyA.getDatatype());

        QueryProperty propertyB = introspection.getProperty("nameB");
        assertNotNull(propertyB);
        assertEquals("nameB", propertyB.getName());
        assertEquals("colB", propertyB.getColName());
        assertEquals(int.class, propertyB.getDatatype());
    }

    interface SupportedTypes
    {
        @FilterProperty
        byte getPropertyA();

        @FilterProperty
        int getPropertyB();

        @FilterProperty
        long getPropertyC();

        @FilterProperty
        float getPropertyD();

        @FilterProperty
        double getPropertyE();

        @FilterProperty
        boolean getPropertyF();

        @FilterProperty
        Byte getPropertyG();

        @FilterProperty
        Integer getPropertyH();

        @FilterProperty
        Long getPropertyI();

        @FilterProperty
        Float getPropertyJ();

        @FilterProperty
        Double getPropertyK();

        @FilterProperty
        Boolean getPropertyL();

        @FilterProperty
        java.util.Date getPropertyM();

        @FilterProperty
        Date getPropertyN();

        @FilterProperty
        Calendar getPropertyO();

        @FilterProperty
        String getPropertyP();

        @FilterProperty
        UUID getPropertyQ();
    }

    interface VoidType
    {
        @FilterProperty
        void getProperty();
    }

    interface UserType
    {
        @FilterProperty
        UserClass getProperty();
    }

    class UserClass
    {
    }

    class BooleanTestClass
    {
        @FilterProperty
        public boolean isValueA()
        {
            return false;
        }

        @FilterProperty(name = "boolB")
        public Boolean isValueB()
        {
            return Boolean.FALSE;
        }

        @FilterProperty
        public boolean getValueC()
        {
            return false;
        }

        @FilterProperty(name = "boolD")
        public Boolean getValueD()
        {
            return Boolean.FALSE;
        }

        @FilterProperty
        public boolean hasValueE()
        {
            return true;
        }

        @FilterProperty(name = "boolF")
        public Boolean hasValueF()
        {
            return Boolean.TRUE;
        }
    }

    class UnannotatedTestClass
    {
        public boolean isValueA()
        {
            return false;
        }

        public Boolean isValueB()
        {
            return Boolean.FALSE;
        }

        public String getPropertyC()
        {
            return null;
        }
    }

    static class StaticTestClass
    {
        @FilterProperty
        public static String getPropertyA()
        {
            return null;
        }

        @FilterProperty
        public String getPropertyB()
        {
            return null;
        }
    }

    static class ProtectedTestClass
    {
        @FilterProperty
        protected String getPropertyA()
        {
            return null;
        }

        @FilterProperty
        public String getPropertyB()
        {
            return null;
        }
    }

    static class WriteOnlyTestClass
    {
        @FilterProperty
        public String getPropertyA()
        {
            return null;
        }

        @FilterProperty
        public void setPropertyB()
        {
        }
    }

    static class OverrideTestClass
    {
        @FilterProperty(colname = "colA", name = "nameA", type = Date.class)
        public String getPropertyA()
        {
            return null;
        }

        @FilterProperty(colname = "colB", name = "nameB", type = int.class)
        public String getPropertyB()
        {
            return null;
        }
    }
}
