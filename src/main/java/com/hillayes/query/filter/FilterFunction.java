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

/**
 * An enumeration of the function allowed in comparison expressions.
 *
 * @author <a href="mailto:watson.phill@gmail.com">Phill Watson</a>
 * @since 1.0.0
 */
public enum FilterFunction
{
    LOWER(false), // convert named string property to LOWER-case
    UPPER(false), // convert named string property to UPPER-case
    CONTAINS(true), // test if named string property CONTAINS a given string
    ENDSWITH(true), // test if named string property ends with a given string
    STARTSWITH(true), // test if named string property starts with a given string
    NOTNULL(false), // test if named property is NOT null
    ISNULL(false); // test if named property is null

    // true if the function takes a value (other than a property) as a parameter.
    private final boolean takesValue;

    private FilterFunction(boolean aTakesValue)
    {
        takesValue = aTakesValue;
    }

    /**
     * Tests whether the function takes a value, other than the a property name, as a parameter.
     *
     * @return <code>true</code> if the function requires a value parameter.
     */
    public boolean takesValue()
    {
        return takesValue;
    }

    public void appendTo(StringBuilder aBuilder, String aColumnName)
    {
        switch (this)
        {
            case CONTAINS:
            case ENDSWITH:
            case STARTSWITH:
                aBuilder.append(aColumnName).append(" like ?");
                break;

            case LOWER:
                aBuilder.append("LOWER(").append(aColumnName).append(')');
                break;

            case UPPER:
                aBuilder.append("UPPER(").append(aColumnName).append(')');
                break;

            case NOTNULL:
                aBuilder.append(aColumnName).append(" is not null");
                break;

            case ISNULL:
                aBuilder.append(aColumnName).append(" is null");
                break;
        }
    }

    /**
     * Formats the given value, in accordance with the function's rules, in readiness for adding the
     * a PreparedStatement's arguments.
     *
     * @param aValue the value to be formatted.
     * @return the formatted value.
     */
    public String formatValue(String aValue)
    {
        if (this == CONTAINS)
            return "%" + aValue.toLowerCase() + "%";

        if (this == STARTSWITH)
            return aValue.toLowerCase() + "%";

        if (this == ENDSWITH)
            return "%" + aValue.toLowerCase();

        return aValue;
    }
}
