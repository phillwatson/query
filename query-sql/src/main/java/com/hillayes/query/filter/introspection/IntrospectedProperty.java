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
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Instant;
import java.time.OffsetTime;
import java.util.*;

/**
 * Represents a property derived from a class property annotated with FilterProperty.
 *
 * @author <a href="mailto:watson.phill@gmail.com">Phill Watson</a>
 * @since 1.0.0
 */
public class IntrospectedProperty extends QueryProperty {
    /**
     * An array of classes that are supported by the filter query. If any other classes are found
     * during the introspection, a {@link UnsupportedDataTypeException} will be raised.
     * <p>
     * Each class entry has a function ({@link ArgumentSetter}) that can coerce a given string value
     * to a compatible value for assignment to a given PreparedStatement arguments.
     */
    static final Map<Class<?>, ArgumentSetter> SUPPORTED_TYPES = new HashMap<>();

    static {
        SUPPORTED_TYPES.put(byte.class, (p, i, v) -> p.setByte(i, Byte.parseByte(v)));
        SUPPORTED_TYPES.put(int.class, (p, i, v) -> p.setInt(i, Integer.parseInt(v)));
        SUPPORTED_TYPES.put(long.class, (p, i, v) -> p.setLong(i, Long.parseLong(v)));
        SUPPORTED_TYPES.put(float.class, (p, i, v) -> p.setFloat(i, Float.parseFloat(v)));
        SUPPORTED_TYPES.put(double.class, (p, i, v) -> p.setDouble(i, Double.parseDouble(v)));
        SUPPORTED_TYPES.put(boolean.class, (p, i, v) -> p.setBoolean(i, Boolean.parseBoolean(v)));
        SUPPORTED_TYPES.put(Byte.class, (p, i, v) -> p.setByte(i, Byte.parseByte(v)));
        SUPPORTED_TYPES.put(Integer.class, (p, i, v) -> p.setInt(i, Integer.parseInt(v)));
        SUPPORTED_TYPES.put(Long.class, (p, i, v) -> p.setLong(i, Long.parseLong(v)));
        SUPPORTED_TYPES.put(Float.class, (p, i, v) -> p.setFloat(i, Float.parseFloat(v)));
        SUPPORTED_TYPES.put(Double.class, (p, i, v) -> p.setDouble(i, Double.parseDouble(v)));
        SUPPORTED_TYPES.put(Boolean.class, (p, i, v) -> p.setBoolean(i, Boolean.parseBoolean(v)));
        SUPPORTED_TYPES.put(BigInteger.class, (p, i, v) -> p.setLong(i, new BigInteger(v).longValue()));
        SUPPORTED_TYPES.put(BigDecimal.class, (p, i, v) -> p.setBigDecimal(i, new BigDecimal(v)));
        SUPPORTED_TYPES.put(String.class, PreparedStatement::setString);
        SUPPORTED_TYPES.put(Currency.class, (p, i, v) -> p.setString(i, Currency.getInstance(v).getCurrencyCode()));
        SUPPORTED_TYPES.put(UUID.class, (p, i, v) -> p.setObject(i, UUID.fromString(v)));
        SUPPORTED_TYPES.put(Date.class, (p, i, v) -> p.setDate(i, new java.sql.Date(Instant.parse(v).toEpochMilli())));
        SUPPORTED_TYPES.put(java.sql.Date.class, (p, i, v) -> p.setDate(i, new java.sql.Date(Instant.parse(v).toEpochMilli())));
        SUPPORTED_TYPES.put(java.sql.Time.class, (p, i, v) -> p.setTime(i, java.sql.Time.valueOf(OffsetTime.parse(v).toLocalTime())));
        SUPPORTED_TYPES.put(Instant.class, (p, i, v) -> p.setTimestamp(i, java.sql.Timestamp.from(Instant.parse(v))));
        SUPPORTED_TYPES.put(java.sql.Timestamp.class, (p, i, v) -> p.setTimestamp(i, java.sql.Timestamp.from(Instant.parse(v))));
        SUPPORTED_TYPES.put(Calendar.class, (p, i, v) -> p.setDate(i, new java.sql.Date(Instant.parse(v).toEpochMilli())));
    }

    public static boolean isSupportedType(Class<?> aClass) {
        return SUPPORTED_TYPES.containsKey(aClass);
    }
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

        if (! isSupportedType(getDatatype())) {
            throw new UnsupportedDataTypeException(getDatatype());
        }
    }

    /**
     * Attempts to set the given value into the indexed argument of the given PreparedStatement. The
     * value will be coerced to the appropriate type; according to the Property's datetype.
     *
     * @param aStatement the PreparedStatement in which the value is to be set.
     * @param aArgIndex the index of the PreparedStatement argument in which the value is to be set.
     * @param aValue the value to be coerced and assigned to the given PreparedStatement.
     * @throws SQLException if aArgIndex does not correspond to a parameter marker in the SQL
     *     statement; if a database access error occurs or this method is called on a closed
     *     PreparedStatement
     */
    public void applyTo(PreparedStatement aStatement, int aArgIndex, String aValue) throws SQLException {
        IntrospectedProperty.SUPPORTED_TYPES.get(getDatatype())
            .setArg(aStatement, aArgIndex, aValue);
    }

    /**
     * A simple functional interface to provide Lambda style functions for assigning values to
     * PreparedStatement arguments.
     */
    private interface ArgumentSetter {
        /**
         * Set the given value into the indexed argument of the given PreparedStatement. The
         * implementation will coerce the value to the appropriate type.
         *
         * @param aStatement the PreparedStatement in which the value is to be set.
         * @param aIndex the index of the PreparedStatement argument in which the value is to be
         *     set.
         * @param aValue the value to be coerced and assigned to the given PreparedStatement.
         */
        void setArg(PreparedStatement aStatement, int aIndex, String aValue) throws SQLException;
    }
}
