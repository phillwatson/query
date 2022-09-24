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

import com.hillayes.query.filter.util.Strings;
import com.hillayes.query.filter.exceptions.FilterComparisonException;
import com.hillayes.query.filter.exceptions.FilterException;
import com.hillayes.query.filter.exceptions.FilterExprException;
import com.hillayes.query.filter.exceptions.InvalidComparisonException;
import com.hillayes.query.filter.exceptions.InvalidOrderByColException;
import com.hillayes.query.filter.exceptions.OrderByConstructException;
import com.hillayes.query.filter.exceptions.UnsupportedDataTypeException;
import com.hillayes.query.filter.introspection.Introspection;
import com.hillayes.query.filter.introspection.Property;
import com.hillayes.query.filter.introspection.PropertyIntrospector;
import com.hillayes.query.filter.parser.FilterParser;
import com.hillayes.query.filter.parser.ParseException;
import com.hillayes.query.filter.parser.TokenMgrError;

import javax.management.Query;
import java.beans.IntrospectionException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author <a href="mailto:watson.phill@gmail.com">Phill Watson</a>
 * @since 1.0.0
 */
public class QueryConstraints {
    private final Class<?> dataClass;

    private Number skip;

    private Number top;

    private String orderBy;

    private String filter;

    /**
     * Constructs a QueryConstraints that applies constraints on the given REST data class. The
     * properties of the REST data class are mapped to the underlying SQL query using the property
     * mappings provided.
     *
     * @param aRestDataClass the REST data class (with FilterProperty annotations).
     */
    public QueryConstraints(Class<?> aRestDataClass) {
        dataClass = aRestDataClass;
    }

    /**
     * The skip option requests the number of leading entities in the queried collection that are to
     * be skipped and not included in the result. A client can request a particular page of entities
     * by combining top and skip.
     * <p>
     * A zero or negative value will mean no "skip" is to be performed.
     *
     * @param aValue the number of leading entities in the collection to be skipped.
     * @return the same QueryConstraints instance - for builder-like construction.
     * @see #setTop(int)
     */
    public QueryConstraints setSkip(int aValue) {
        skip = (aValue > 0) ? aValue : null;
        return this;
    }

    /**
     * Returns the number of leading entities in the queried collection that are to be skipped and
     * not included in the result.
     * <p>
     * A <code>null</code>, negative or zero value will mean no "skip" is to be performed.
     *
     * @return the number of leading entities to be skipped.
     */
    public Number getSkip() {
        return skip;
    }

    /**
     * The top option requests the number of entities in the queried collection to be included in
     * the result. A client can request a particular page of entities by combining top and skip.
     * <p>
     * A zero or negative value will mean no limit is to be applied.
     *
     * @param aValue the number of entities to be included in the result.
     * @return the same QueryConstraints instance - for builder-like construction.
     * @see #setSkip(int)
     */
    public QueryConstraints setTop(int aValue) {
        top = (aValue > 0) ? aValue : null;
        return this;
    }

    /**
     * Returns the number of entities in the queried collection to be included in the result.
     * <p>
     * A <code>null</code>, negative or zero value will mean no limit is to be applied.
     *
     * @return the number of entities to be included in the result
     */
    public Number getTop() {
        return top;
    }

    /**
     * Returns the orderBy clause.
     */
    public String getOrderBy() {
        return orderBy;
    }

    /**
     * Sets the order by which the entities are returned.
     *
     * @param aValue the order-by clause.
     */
    public void setOrderBy(String aValue) {
        orderBy = Strings.isEmpty(aValue) ? null : aValue;
    }

    /**
     * Sets the filter expression used to restrict the entities returned by the final query.
     *
     * @param aValue the REST URL query filter value.
     */
    public void setFilter(String aValue) {
        filter = Strings.isEmpty(aValue) ? null : aValue;
    }

    /**
     * Returns the filter expression to be applied.
     */
    public String getFilter() {
        return filter;
    }

    /**
     * Tests whether there are any constraints defined in this QueryConstraints. If
     * <code>true</code> then calling the {@link #applyTo(Query)} method will have no effect on the
     * given query.
     *
     * @return <code>true</code> if there are constraints to apply.
     */
    public boolean isEmpty() {
        if (skip != null) {
            return false;
        }

        if (top != null) {
            return false;
        }

        if (orderBy != null) {
            return false;
        }

        if (filter != null) {
            return false;
        }

        return true;
    }

    /**
     * Parses the given String as the value of the $orderby clause. The clause is made up of an
     * ordered, comma-separated collection of propName names. For example "name,lastModified,
     * owner".
     * <p>
     * Each propName name can be qualified with "asc" or "desc" to specify the direction on which
     * that propName should be sorted. If not specified, the propName will be sorted ascending.
     * Examples would be "name asc", "name desc, lastmodified asc".
     *
     * @param aDataIntrospection the introspection information for the data-class.
     * @throws FilterException if the clause is invalid.
     */
    private List<OrderByCol> parseOrderBy(Introspection aDataIntrospection) throws FilterException {
        List<OrderByCol> result = null;

        if (!Strings.isEmpty(orderBy)) {
            // split the comma-delimited columns into an ordered list
            for (String element : orderBy.split(",")) {
                OrderByCol col = OrderByCol.parse(element, aDataIntrospection);
                if (col != null) {
                    if (result == null) {
                        result = new ArrayList<>();
                    }
                    result.add(col);
                }
            }
        }

        return result;
    }

    /**
     * Creates a PreparedStatement from the query constraints, using the given JDBC Connection as a
     * factory. The columns to be selected will be specified by the given projection string. For
     * example;
     *
     * <pre>
     * "select a, b, c from table"
     * </pre>
     *
     * @throws FilterComparisonException if a comparison is not valid for data type.
     * @throws FilterExprException if the filter is not syntactically valid.
     * @param aConnection the JDBC connection from which the PreparedStatement is obtained.
     * @param aProjection the SQL select statement specifying the columns and table to be selected.
     * @return a PreparedStatement with the given projection and the query constraints applied.
     * @throws SQLException if a database access error occursor this method is called on a closed
     * connection.
     * @throws IntrospectionException if an exception occurs during introspection.
     * @throws UnsupportedDataTypeException if any identified property is not of a supported type.
     */
    public PreparedStatement prepareStatement(Connection aConnection, String aProjection)
        throws SQLException, UnsupportedDataTypeException, IntrospectionException {
        StringBuilder sql = new StringBuilder(aProjection);

        Introspection introspection = null;
        if ((filter != null) || (orderBy != null)) {
            introspection = PropertyIntrospector.introspect(dataClass);
        }

        QueryContext context = null;
        if (filter != null) {
            context = new DataClassQueryContext(introspection);
            try {
                FilterParser.parse(context, filter);
                sql.append(" WHERE ").append(context.queryBuilder());
            } catch (InvalidComparisonException e) {
                throw new FilterComparisonException(filter, e.getComparison(), e);
            } catch (ParseException | TokenMgrError e) {
                throw new FilterExprException(filter, e);
            }
        }

        if (orderBy != null) {
            sql.append(" ORDER BY ");

            boolean first = true;
            for (OrderByCol order : parseOrderBy(introspection)) {
                if (!first)
                    sql.append(", ");
                sql.append(order.getProperty().getColName()).append(" ").append(order.ascending ? "ASC" : "DESC");
                first = false;
            }
        }

        if (skip != null) {
            sql.append(" OFFSET ").append(skip);
        }

        if (top != null) {
            sql.append(" LIMIT ").append(top);
        }

        PreparedStatement result = aConnection.prepareStatement(sql.toString());

        if (context != null) {
            context.applyArg(result, 1);
        }

        return result;
    }

    @Override
    public String toString() {
        return "QueryConstraints [dataClass=" + dataClass.getName() + ", skip=" + skip + ", top=" + top + ", orderBy="
            + orderBy + ", filter=" + filter + "]";
    }

    /**
     * Encapsulates an element within a $orderby clause. Each element names a propName on which to
     * sort the data and the order in which that propName should be sorted (asc or desc).
     */
    public static class OrderByCol {
        // the property on which the order will be based.
        Property property;

        boolean ascending = true;

        /**
         * A factor method to parse a single element of a $orderby clause. Examples would be "name
         * desc" or "lastModified asc"; where the "desc" and "asc" are optional.
         * <p>
         * If the given String is <code>null</code> (or empty), the return value will be
         * <code>null</code>.
         *
         * @param aRawData the $orderby element to be parsed.
         * @param aDataClass the introspection information about the data-class from which the
         * order-by property is derived.
         * @return the parsed OrderByCol element.
         * @throws FilterException if the $orderby element is not a valid construct.
         */
        public static OrderByCol parse(String aRawData, Introspection aDataClass) throws FilterException {
            if (Strings.isEmpty(aRawData)) {
                return null;
            }

            // propName may be followed by an "asc" or "desc"
            String[] elements = aRawData.trim().split("\\s+");

            // must only be name and optional "asc"/"desc"
            if (elements.length > 2) {
                throw new OrderByConstructException(aRawData);
            }

            Property propInfo = aDataClass.getProperty(elements[0]);
            if (propInfo == null) {
                throw new InvalidOrderByColException(elements[0]);
            }

            // determine if it's "asc" or "desc"
            boolean asc = true;
            if (elements.length > 1) {
                elements[1] = elements[1].trim().toLowerCase();
                if ("desc".equals(elements[1])) {
                    asc = false;
                } else if (!"asc".equals(elements[1])) {
                    throw new OrderByConstructException(aRawData);
                }
            }

            return new OrderByCol(propInfo, asc);
        }

        private OrderByCol(Property aProperty, boolean aAscending) {
            property = aProperty;
            ascending = aAscending;
        }

        /**
         * Returns the name of the data class property on which to perform the order by.
         */
        public Property getProperty() {
            return property;
        }

        /**
         * Whether the ordering is ascending (<code>true</code>) or descending (<code>false</code>).
         */
        public boolean isAscending() {
            return ascending;
        }
    }
}
