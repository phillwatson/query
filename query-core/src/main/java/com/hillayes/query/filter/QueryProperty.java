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
package com.hillayes.query.filter;

import com.hillayes.query.filter.exceptions.UnsupportedDataTypeException;

/**
 * Represents a property that can be referenced in a filter expression.
 *
 * @author <a href="mailto:watson.phill@gmail.com">Phill Watson</a>
 * @since 1.0.0
 */
public class QueryProperty implements Queryable {
    // the name of the property as given in the filter expression.
    private final String name;

    // the name of the property as written to the generated SQL expression.
    private final String colName;

    // the data type of the property.
    private final Class<?> datatype;

    /**
     * Constructs a Property instance from the annotation placed of a Bean property getter method,
     * taking the given values as defaults when the annotation does not specify them.
     *
     * @param aName the value for the property name.
     * @param aColName the SQL name of the property.
     * @param aDataType the class for the property's data type.
     * @throws UnsupportedDataTypeException if the give class not of a supported type.
     */
    public QueryProperty(String aName, String aColName, Class<?> aDataType) {
        name = aName;
        colName = aColName;
        datatype = aDataType;
    }

    /**
     * Returns the name by which the property is identified in the filter expression.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the name by which the property is identified in the generated SQL query.
     */
    public String getColName() {
        return colName;
    }

    /**
     * Return the data type to which the value in the filter expression will be coerced.
     */
    public Class<?> getDatatype() {
        return datatype;
    }

    @Override
    public String toString() {
        return "Property [name=" + name + ", colName=" + colName + ", datatype=" + datatype.getName() + "]";
    }
}
