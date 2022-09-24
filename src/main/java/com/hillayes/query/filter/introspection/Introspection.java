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

import com.hillayes.query.filter.FilterProperty;
import com.hillayes.query.filter.exceptions.UnsupportedDataTypeException;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Records the properties that can be included in a filter query. These are derived by introspecting
 * the getter methods of a given class, or interface, and their super-classes/interfaces. Only those
 * getter methods that carry the {@link FilterProperty} annotation will be included.
 *
 * @author <a href="mailto:watson.phill@gmail.com">Phill Watson</a>
 * @since 1.0.0
 */
public class Introspection {
    /**
     * The class, or interface, for which this introspection was performed.
     */
    private final Class<?> dataClass;

    /**
     * The properties derived from the given data class.
     */
    private final Map<String, Property> properties = new HashMap<>();

    /**
     * Creates a new introspection for the given data class. All getter methods annotated with the
     * {@link FilterProperty} will be gathered and recorded.
     *
     * @param aDataClass the class to be introspected.
     * @throws IntrospectionException       if an exception occurs during introspection.
     * @throws UnsupportedDataTypeException if any identified property is not of a supported
     *                                      type.
     */
    Introspection(Class<?> aDataClass) throws IntrospectionException, UnsupportedDataTypeException {
        dataClass = aDataClass;
        introspect();
    }

    /**
     * @throws IntrospectionException       if an exception occurs during introspection.
     * @throws UnsupportedDataTypeException if the any identified property is not of a supported
     *                                      type.
     */
    private void introspect() throws IntrospectionException, UnsupportedDataTypeException {
        if (dataClass.isInterface()) {
            addBeanInfo(Introspector.getBeanInfo(dataClass));
        } else {
            addBeanInfo(Introspector.getBeanInfo(dataClass, Object.class));
        }

        // include all interfaces
        for (Class<?> superInterface : getAllInterfaces(dataClass, new HashSet<>())) {
            addBeanInfo(Introspector.getBeanInfo(superInterface));
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

    /**
     * Adds the properties described by the given BeanInfo to the introspection result.
     *
     * @param aBeanInfo the BeanInfo describing the properties to be included in the introspection.
     * @throws UnsupportedDataTypeException if the any identified property is not of a supported
     *                                      type.
     */
    private void addBeanInfo(BeanInfo aBeanInfo) throws UnsupportedDataTypeException {
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

            Property property = new Property(descriptor, filterProperty);
            properties.put(property.getName(), property);
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
            Class<?> returnType = method.getReturnType();

            // if it's a Boolean whose name starts with "is"
            if ((method.getReturnType() == Boolean.class) && (name.startsWith("is"))) {
                // if the method does NOT hold the requried annotation
                FilterProperty filterProperty = method.getAnnotation(FilterProperty.class);
                if (filterProperty == null) {
                    continue;
                }

                // remove leading "is" and change capitalization
                name = Introspector.decapitalize(name.substring(2));
                Property property = new Property(filterProperty, name, returnType);
                properties.put(property.getName(), property);
            }
        }
    }

    /**
     * Returns an unmodifiable collection of the properties derived from the introspected data
     * class.
     */
    public Collection<Property> getProperties() {
        return Collections.unmodifiableCollection(properties.values());
    }

    /**
     * Returns the details of the named data class property, or <code>null</code> if not such named
     * property was found.
     *
     * @param aName the name of the property to be described.
     * @return the description of the named property, or <code>null</code>.
     */
    public Property getProperty(String aName) {
        return properties.get(aName);
    }
}
