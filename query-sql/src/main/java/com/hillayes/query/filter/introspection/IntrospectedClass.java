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

import com.hillayes.query.filter.QueryClass;
import com.hillayes.query.filter.exceptions.UnsupportedDataTypeException;
import com.hillayes.query.filter.util.Strings;

import java.beans.*;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Extends QueryClass derived from a class whose properties are annotated with
 * FilterProperty. It will recursively introspect those classes references by
 * the annotated properties.
 * <p>
 * To improve performance, it will cache the introspection results for a given
 * class.
 *
 * @author <a href="mailto:watson.phill@gmail.com">Phill Watson</a>
 * @since 1.0.0
 */
public class IntrospectedClass extends QueryClass {
    private static final Map<String, IntrospectedClass> cache = new HashMap<>();

    /**
     * A factory method to return an introspection of the given class. If the
     * class has already been introspected, the cached introspection will be
     * returned.
     *
     * @param aDataClass the class to be introspected.
     * @return the introspection of the given class.
     */
    public static IntrospectedClass introspect(Class<?> aDataClass) {
        if (aDataClass == null) {
            throw new NullPointerException("A class must be provided for introspection.");
        }

        IntrospectedClass result = cache.get(aDataClass.getName());
        if (result == null) {
            result = new IntrospectedClass(aDataClass);
            cache.put(aDataClass.getName(), result);
        }
        return result;
    }

    private IntrospectedClass(Class<?> aDataClass) {
        super(aDataClass);

        try {
            addBeanInfo(aDataClass.isInterface()
                ? Introspector.getBeanInfo(aDataClass)
                : Introspector.getBeanInfo(aDataClass, Object.class));

            // include all interfaces
            for (Class<?> superInterface : getAllInterfaces(aDataClass, new HashSet<>())) {
                addBeanInfo(Introspector.getBeanInfo(superInterface));
            }
        } catch (IntrospectionException e) {
            throw new UnsupportedDataTypeException(aDataClass, e);
        }
    }

    /**
     * Adds the properties described by the given BeanInfo to the introspection result.
     *
     * @param aBeanInfo the BeanInfo describing the properties to be included in the introspection.
     */
    private void addBeanInfo(BeanInfo aBeanInfo) throws IntrospectionException {
        booleanCheck(aBeanInfo);

        for (PropertyDescriptor descriptor : aBeanInfo.getPropertyDescriptors()) {
            Method method = descriptor.getReadMethod();
            if (method == null) {
                continue;
            }

            // if the method does NOT hold the required annotation
            FilterProperty filterProperty = method.getAnnotation(FilterProperty.class);
            if (filterProperty == null) {
                continue;
            }

            Class<?> dataClass = (filterProperty.type() == Void.class)
                ? descriptor.getPropertyType()
                : filterProperty.type();

            String name = Strings.isEmpty(filterProperty.name())
                ? descriptor.getName()
                : filterProperty.name();

            addProperty(name, IntrospectedProperty.isSupportedType(dataClass)
                ? new IntrospectedProperty(descriptor, filterProperty)
                : IntrospectedClass.introspect(descriptor.getPropertyType()));
        }
    }

    /**
     * As Boolean (the class) getter methods aren't included in the Introspector's list of
     * properties, this will search for those specific getter methods.
     *
     * @param aBeanInfo the BeanInfo describing the properties to be included in the introspection.
     */
    private void booleanCheck(BeanInfo aBeanInfo) {
        for (MethodDescriptor descriptor : aBeanInfo.getMethodDescriptors()) {
            Method method = descriptor.getMethod();
            String name = method.getName();

            // if it's a Boolean whose name starts with "is"
            if ((method.getReturnType() == Boolean.class) && (name.startsWith("is"))) {
                // if the method does NOT hold the requried annotation
                FilterProperty filterProperty = method.getAnnotation(FilterProperty.class);
                if (filterProperty == null) {
                    continue;
                }

                // remove leading "is" and change capitalization
                name = Strings.isEmpty(filterProperty.name())
                    ? Introspector.decapitalize(name.substring(2))
                    : filterProperty.name();

                addProperty(name, new IntrospectedProperty(filterProperty, name, Boolean.class));
            }
        }
    }

    /**
     * Recursively adds the interfaces implemented by, or extended by, the given class, or
     * interface.
     *
     * @param aClass       the class, or interface, to be searched.
     * @param aAccumulator the accumulation (and final result) of the search.
     * @return the collection of interfaces found.
     */
    private Set<Class<?>> getAllInterfaces(Class<?> aClass, Set<Class<?>> aAccumulator) {
        for (Class<?> candidate : aClass.getInterfaces()) {
            if (aAccumulator.add(candidate)) {
                getAllInterfaces(candidate, aAccumulator);
            }
        }

        return aAccumulator;
    }
}
