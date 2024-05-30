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
import java.lang.reflect.Method;
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
     * A map of supported classes and the lambda function ({@link ArgumentSetter})
     * that can coerce a given string value to a compatible value for assignment
     * to a given PreparedStatement arguments.
     */
    static final Map<Class<?>, ArgumentSetter> SQL_SETTERS = new HashMap<>();

    static {
        SQL_SETTERS.put(byte.class, (p, i, v) -> p.setByte(i, Byte.parseByte(v)));
        SQL_SETTERS.put(int.class, (p, i, v) -> p.setInt(i, Integer.parseInt(v)));
        SQL_SETTERS.put(long.class, (p, i, v) -> p.setLong(i, Long.parseLong(v)));
        SQL_SETTERS.put(float.class, (p, i, v) -> p.setFloat(i, Float.parseFloat(v)));
        SQL_SETTERS.put(double.class, (p, i, v) -> p.setDouble(i, Double.parseDouble(v)));
        SQL_SETTERS.put(boolean.class, (p, i, v) -> p.setBoolean(i, Boolean.parseBoolean(v)));
        SQL_SETTERS.put(Byte.class, (p, i, v) -> p.setByte(i, Byte.parseByte(v)));
        SQL_SETTERS.put(Integer.class, (p, i, v) -> p.setInt(i, Integer.parseInt(v)));
        SQL_SETTERS.put(Long.class, (p, i, v) -> p.setLong(i, Long.parseLong(v)));
        SQL_SETTERS.put(Float.class, (p, i, v) -> p.setFloat(i, Float.parseFloat(v)));
        SQL_SETTERS.put(Double.class, (p, i, v) -> p.setDouble(i, Double.parseDouble(v)));
        SQL_SETTERS.put(Boolean.class, (p, i, v) -> p.setBoolean(i, Boolean.parseBoolean(v)));
        SQL_SETTERS.put(BigInteger.class, (p, i, v) -> p.setLong(i, new BigInteger(v).longValue()));
        SQL_SETTERS.put(BigDecimal.class, (p, i, v) -> p.setBigDecimal(i, new BigDecimal(v)));
        SQL_SETTERS.put(String.class, PreparedStatement::setString);
        SQL_SETTERS.put(Currency.class, (p, i, v) -> p.setString(i, Currency.getInstance(v).getCurrencyCode()));
        SQL_SETTERS.put(UUID.class, (p, i, v) -> p.setObject(i, UUID.fromString(v)));
        SQL_SETTERS.put(Date.class, (p, i, v) -> p.setDate(i, new java.sql.Date(Instant.parse(v).toEpochMilli())));
        SQL_SETTERS.put(java.sql.Date.class, (p, i, v) -> p.setDate(i, new java.sql.Date(Instant.parse(v).toEpochMilli())));
        SQL_SETTERS.put(java.sql.Time.class, (p, i, v) -> p.setTime(i, java.sql.Time.valueOf(OffsetTime.parse(v).toLocalTime())));
        SQL_SETTERS.put(Instant.class, (p, i, v) -> p.setTimestamp(i, java.sql.Timestamp.from(Instant.parse(v))));
        SQL_SETTERS.put(java.sql.Timestamp.class, (p, i, v) -> p.setTimestamp(i, java.sql.Timestamp.from(Instant.parse(v))));
        SQL_SETTERS.put(Calendar.class, (p, i, v) -> p.setDate(i, new java.sql.Date(Instant.parse(v).toEpochMilli())));
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
        this(aAnnotation, aDescriptor.getName(), aDescriptor.getPropertyType(), aDescriptor.getReadMethod());
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
    IntrospectedProperty(FilterProperty aAnnotation,
                         String aDefaultName,
                         Class<?> aDefaultType,
                         Method aGetterMethod) {
        super(aGetterMethod,
            Strings.isEmpty(aAnnotation.name()) ? aDefaultName : aAnnotation.name(),
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
        IntrospectedProperty.SQL_SETTERS.get(getDatatype())
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
