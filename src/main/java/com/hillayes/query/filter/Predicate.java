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

import com.hillayes.query.filter.exceptions.InvalidPropertyRefException;
import com.hillayes.query.filter.introspection.Property;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Represents the comparison of a filter property, parsed from the filter expression. Instances of
 * this class are constructed by the QueryContext and populated by the FilterParser. It records the
 * elements of a single comparison expression; e.g. the property name, the expression operator and
 * value.
 *
 * @author <a href="mailto:watson.phill@gmail.com">Phill Watson</a>
 * @since 1.0.0
 */
public class Predicate
{
    private QueryContext context;

    private Property property;

    private FilterFunction function;

    private String name;

    private Operator operator;

    private String value;

    private boolean isNumeric;

    public Predicate(QueryContext aContext)
    {
        context = aContext;
    }

    /**
     * Returns the function that was referenced in the expression, if any.
     *
     * @return any function that was referenced in the comparison.
     */
    public FilterFunction getFunction()
    {
        return function;
    }

    /**
     * Called by the FilterParser to set the function that was referenced in the expression, if any.
     *
     * @param aValue the function to be set.
     */
    public void setFunction(FilterFunction aValue)
    {
        function = aValue;
    }

    /**
     * Returns the name of the property that was referenced in the expression.
     *
     * @return the name of the property referenced in the expression.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Called by the FilterParser to set the name of the property that was referenced in the
     * expression.
     *
     * @param aValue the name of the property referenced in the expression.
     * @throws InvalidPropertyRefException if the named property cannot be used in a filter.
     */
    public void setName(String aValue)
    {
        name = aValue;

        property = context.getPropertyFor(name);
        if (property == null)
        {
            throw new InvalidPropertyRefException(name);
        }
    }

    /**
     * Returns the comparison operator that was referenced in the expression, if any.
     *
     * @return the comparison operator referenced in the expression.
     */
    public Operator getOperator()
    {
        return operator;
    }

    /**
     * Called by the FilterParser to set the comparison operator that was referenced in the
     * expression, if any.
     *
     * @param aValue the comparison operator referenced in the expression.
     */
    public void setOperator(Operator aValue)
    {
        operator = aValue;
    }

    /**
     * Called by the FilterParser to set the value of the comparison to an identifier value. That
     * is, a value that is neither a string nor a numeric constant. After calling this method, the
     * method {@link #isNumeric()} will return <code>false</code>.
     * <p>
     * This method might be used when the comparison's property is an enumeration; although, in such
     * cases, the {@link #setString(String)} would also suffice.
     *
     * @param aValue the identifier name.
     */
    public void setIdentifier(String aValue)
    {
        value = aValue.trim();
        if ("null".equals(value))
        {
            value = null;
        }
        isNumeric = false;
    }

    /**
     * Called by the FilterParser to set the value of the comparison to a quoted string value. The
     * quotes will be removed. After calling this method, the method {@link #isNumeric()} will
     * return <code>false</code>.
     *
     * @param aValue the quoted string value.
     */
    public void setString(String aValue)
    {
        value = aValue.trim();

        char char1 = value.charAt(0);
        if ((char1 == '\'') || (char1 == '"'))
        {
            int len = value.length();
            value = value.substring(1).substring(0, len - 2);
        }
        isNumeric = false;
    }

    /**
     * Called by the FilterParser to set the value of the comparison to a numeric value. After
     * calling this method, the method {@link #isNumeric()} will return <code>true</code>.
     *
     * @param aValue the numeric value (in string form).
     */
    public void setNumber(String aValue)
    {
        value = aValue.trim();
        isNumeric = true;
    }

    /**
     * Returns the value used in the comparison expression - the right-hand-side of the comparison.
     * This will be any of the {@link #setString() String}, {@link #setIdentifier(String)
     * Identifier} or {@link #setNumber(String) Number} values set be the FilterParser.
     *
     * @return the value used in the comparison expression.
     */
    public String getValue()
    {
        return value;
    }

    /**
     * Tests whether the value held by this Predicate is a numeric constant. This will only return
     * true if the value was set using the {@link #setNumber(String)} method.
     *
     * @return <code>true</code> if the value is a numeric constant.
     */
    public boolean isNumeric()
    {
        return isNumeric;
    }

    /**
     * Tests whether the given Predicate is a valid comparison. For example, it may ensure that the
     * name identifies a known property and that the value is compatible with the property's type.
     *
     * @param aComparison the Predicate to be validated.
     * @return <code>true</code> if the given Predicate is valid. Otherwise <code>false</code>.
     */
    public boolean isValid()
    {
        return true;
    }

    /**
     * Applies the value of this predicate to the arguments of the given PreparedStatement. The
     * index of the argument is provided. The value can be coerced and formatted according to the
     * data type of the Property's named propName, and any function the Predicate references.
     *
     * @param aStatement the PreparedStatement to which the Predicate's value is to be applied.
     * @param aArgIndex the index of the PreparedStatement's argument place-holder to which the
     * value is to be set.
     * @return the next argument index value.
     * @throws SQLException if aArgIndex does not correspond to a parameter marker in the SQL
     * statement; if a database access error occurs or this method is called on a closed
     * PreparedStatement
     */
    public int applyArg(PreparedStatement aStatement, int aArgIndex) throws SQLException
    {
        // if the expresion references a value
        if ((operator != null) || ((function != null) && (function.takesValue())))
        {
            // apply the formatted property value to the statement
            property.applyTo(aStatement, aArgIndex++, (function != null) ? function.formatValue(value) : value);
        }

        return aArgIndex;
    }

    /**
     * Adds the predicate expression to the given SQL query string.
     */
    public void appendTo(StringBuilder aQuery)
    {
        if (function != null)
        {
            function.appendTo(aQuery, property.getColName());
        }
        else
        {
            aQuery.append(property.getColName());
        }

        if (operator != null)
        {
            aQuery.append(' ').append(operator.getMnemonic()).append(" ?");
        }
    }

    /**
     * Constructs a String representation of the Predicate. This is intended for debugging.
     *
     * @see Object#toString()
     */
    @Override
    public String toString()
    {
        StringBuilder result = new StringBuilder();
        appendTo(result);

        return result.toString();
    }
}
