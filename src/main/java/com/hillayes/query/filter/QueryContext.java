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

import com.hillayes.query.filter.introspection.Property;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * A QueryContext implementation is passed to the FilterParser to allow the parser to create and
 * validate instances of Predicate to record the comparisons it encounters in the filter expression.
 *
 * @author <a href="mailto:watson.phill@gmail.com">Phill Watson</a>
 * @since 1.0.0
 */
public interface QueryContext
{
    /**
     * Returns the introspection info for the names property.
     *
     * @param aName the property whose info is requested.
     * @return the named property's info, or <code>null</code> if not found.
     */
    public Property getPropertyFor(String aName);

    /**
     * A factory method to create an instance of Predicate. The implementor can decide whether to
     * return a sub-class of Predicate that best suits their needs.
     *
     * @return a new, empty instance of Predicate.
     */
    public Predicate newPredicate();

    /**
     * Provides access to the StringBuilder used to construct the SQL query during parsing.
     *
     * @return the SQL query StringBuilder.
     */
    public StringBuilder queryBuilder();

    /**
     * Applies the predicates, found during the parsing of the query, to the arguments of the given
     * PreparedStatement. The argument index to which the initial predicate is provided. Subsequent
     * predicates will be applied to consecutive indices.
     *
     * @param aStatement the PreparedStatement to which the Predicate's value is to be applied.
     * @param aArgIndex the initial index of the PreparedStatement's argument place-holder to which
     * the predicates are to be set.
     * @throws SQLException if aArgIndex (or subsequent indices) does not correspond to a
     * parametermarker in the SQL statement; if a database access error occurs or this method is
     * called on a closed PreparedStatement
     */
    public void applyArg(PreparedStatement aStatement, int aArgIndex) throws SQLException;
}
