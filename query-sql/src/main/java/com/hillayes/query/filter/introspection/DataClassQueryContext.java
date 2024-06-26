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

import com.hillayes.query.filter.Predicate;
import com.hillayes.query.filter.QueryClass;
import com.hillayes.query.filter.QueryContext;
import com.hillayes.query.filter.QueryProperty;

import java.beans.IntrospectionException;
import java.util.ArrayList;
import java.util.List;

/**
 * An implementation of QueryContext that uses the introspection of a given
 * data class to derive information used to verify named properties within
 * the query.
 *
 * @author <a href="mailto:watson.phill@gmail.com">Phill Watson</a>
 * @since 1.0.0
 */
public class DataClassQueryContext implements QueryContext {
    private final QueryClass queryClass;

    private final StringBuilder query = new StringBuilder();

    private final List<Predicate> predicates = new ArrayList<>();

    /**
     * Constructs a QueryContext that uses the introspection of the given data class as the basis
     * for validation of the property names within a filter query.
     */
    public DataClassQueryContext(Class<?> aDataClass) throws IntrospectionException {
        queryClass = IntrospectedClass.introspect(aDataClass);
    }

    @Override
    public String getClassName() {
        return queryClass.getDataClass().getName();
    }

    /**
     * Returns the introspection info for the named property.
     *
     * @param aName the property whose info is requested.
     * @return the named property's info, or <code>null</code> if not found.
     */
    @Override
    public QueryProperty getPropertyFor(String aName) {
        return queryClass.getProperty(aName);
    }

    @Override
    public Predicate newPredicate() {
        // create a new Predicate and add to ordered list
        Predicate result = new Predicate(this);
        predicates.add(result);

        return result;
    }

    @Override
    public Iterable<? extends Predicate> getPredicates() {
        return predicates;
    }

    @Override
    public StringBuilder queryBuilder() {
        return query;
    }
}
