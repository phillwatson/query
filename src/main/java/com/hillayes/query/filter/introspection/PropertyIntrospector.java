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

import com.hillayes.query.filter.exceptions.UnsupportedDataTypeException;

import java.beans.IntrospectionException;
import java.util.HashMap;
import java.util.Map;

/**
 * Introspects a given class whose properties and / or methods have been annotated with the
 * FilterProperty annotation. It will recursively introspect the super-classes of the given class
 * and also introspect (in the same fashion) those FilterProperty properties it finds whose class is
 * a non-primitive.
 * <p>
 * The result will be returned as a Map of the property types (their class), keyed on the property
 * paths. The path will be the name of the property. In the case of nested properties the path will
 * be a dot-delimited value.
 *
 * @author <a href="mailto:watson.phill@gmail.com">Phill Watson</a>
 * @since 1.0.0
 */
public final class PropertyIntrospector {
    private PropertyIntrospector() {
    }

    private static Map<String, Introspection> history = new HashMap<>();

    /**
     * Recursively introspects the given class and adds any annotated properties to the result.
     *
     * @param aDataClass the class to be introspected.
     * @return the property introspection of the given class.
     * @throws IntrospectionException if an exception occurs during introspection.
     * @throws UnsupportedDataTypeException if any identified property is not of a supported type.
     */
    public static Introspection introspect(Class<?> aDataClass)
        throws IntrospectionException, UnsupportedDataTypeException {
        if (aDataClass == null) {
            throw new NullPointerException("A class must be provided for introspection.");
        }

        String className = aDataClass.getName();
        Introspection result = history.get(className);
        if (result == null) {
            result = new Introspection(aDataClass);
            history.put(className, result);
        }

        return result;
    }
}
