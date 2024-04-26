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

import com.hillayes.query.filter.QueryProperty;
import com.hillayes.query.filter.exceptions.UnsupportedDataTypeException;
import com.hillayes.query.filter.util.Strings;

import java.beans.PropertyDescriptor;

/**
 * Represents a property derived from a class property annotated with FilterProperty.
 *
 * @author <a href="mailto:watson.phill@gmail.com">Phill Watson</a>
 * @since 1.0.0
 */
public class IntrospectedProperty extends QueryProperty {
    /**
     * Constructs a Property instance from the given property values derived from the introspection
     * of a Bean property, and the annotation placed on the Bean property getter method.
     *
     * @param aDescriptor the introspection of a Bean property.
     * @param aAnnotation the FilterProperty annotation of the Bean property getter method.
     * @throws UnsupportedDataTypeException if the give class not of a supported type.
     */
    IntrospectedProperty(PropertyDescriptor aDescriptor, FilterProperty aAnnotation) {
        this(aAnnotation, aDescriptor.getName(), aDescriptor.getPropertyType());
    }

    /**
     * Constructs a Property instance from the annotation placed of a Bean property getter method,
     * taking the given values as defaults when the annotation does not specify them.
     *
     * @param aAnnotation  the FilterProperty annotation of the Bean property getter method.
     * @param aDefaultName the default value for the property name, and propName name.
     * @param aDefaultType the default class for the property's data type.
     * @throws UnsupportedDataTypeException if the give class not of a supported type.
     */
    IntrospectedProperty(FilterProperty aAnnotation, String aDefaultName, Class<?> aDefaultType) {
        super(Strings.isEmpty(aAnnotation.name()) ? aDefaultName : aAnnotation.name(),
             Strings.isEmpty(aAnnotation.colname()) ? aDefaultName : aAnnotation.colname(),
             (aAnnotation.type() == Void.class) ? aDefaultType : aAnnotation.type());
    }
}
