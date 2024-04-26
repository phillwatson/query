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

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * A QueryContext implementation is passed to the FilterParser to allow the parser to create and
 * validate instances of Predicate to record the comparisons it encounters in the filter expression.
 *
 * @author <a href="mailto:watson.phill@gmail.com">Phill Watson</a>
 * @since 1.0.0
 */
public interface QueryContext {
    /**
     * Returns the name of the class for which the query is to be created.
     */
    String getClassName();

    /**
     * Returns the introspection info for the named property.
     *
     * @param aName the property whose info is requested.
     * @return the named property's info, or <code>null</code> if not found.
     */
    QueryProperty getPropertyFor(String aName);

    /**
     * A factory method to create an instance of Predicate. The implementor can decide whether to
     * return a sub-class of Predicate that best suits their needs.
     *
     * @return a new, empty instance of Predicate.
     */
    Predicate newPredicate();

    /**
     * Provides access to the StringBuilder used to construct the SQL query during parsing.
     *
     * @return the SQL query StringBuilder.
     */
    StringBuilder queryBuilder();

    /**
     * Applies the predicates, found during the parsing of the query, to the arguments of the given
     * PreparedStatement.
     *
     * @param aStatement the PreparedStatement to which the Predicate's value is to be applied.
     * @throws SQLException if aArgIndex (or subsequent indices) does not correspond to a
     * parametermarker in the SQL statement; if a database access error occurs or this method is
     * called on a closed PreparedStatement
     */
    default void applyArgs(PreparedStatement aStatement) throws SQLException {
        applyArgs(aStatement, 1);
    }

    /**
     * Applies the predicates, found during the parsing of the query, to the arguments of the given
     * PreparedStatement. The first arguments are applied to the given index, subsequent arguments
     * are applied consecutively.
     *
     * @param aStatement the PreparedStatement to which the Predicate's value is to be applied.
     * @param aArgIndex the (1-based) index of the first argument in the prepared statement.
     * @throws SQLException if aArgIndex (or subsequent indices) does not correspond to a
     * parametermarker in the SQL statement; if a database access error occurs or this method is
     * called on a closed PreparedStatement
     */
    void applyArgs(PreparedStatement aStatement, int aArgIndex) throws SQLException;
}
