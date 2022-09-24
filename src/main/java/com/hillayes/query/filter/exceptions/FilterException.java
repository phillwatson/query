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
package com.hillayes.query.filter.exceptions;

/**
 * The base for all REST query constraint violations. These cover the query constraints such as
 * $skip, $top, %count, $filter, $orderby.
 *
 * @author pwatson
 * @since 21 Aug 2018
 */
public abstract class FilterException extends RuntimeException {
    private final ParameterizedObject parameters = new ParameterizedObject();

    public FilterException(ErrorCode aErrorCode) {
        this(aErrorCode, null);
    }

    public FilterException(ErrorCode aErrorCode, Throwable aCause) {
        super(aErrorCode.getMessage(), aCause);
    }

    /**
     * Returns the contextual parameters.
     *
     * @return the contextual parameters.
     */
    public final ParameterizedObject getParameters() {
        return parameters;
    }

    public enum ErrorCode {
        INVALID_PROPERTY_REF("The named property is not available to filter expressions."),
        INVALID_FILTER_EXPRESSION("Invalid filter expression."),
        INVALID_ORDERBY_COL("The named property is not available to the order-by clause."),
        INVALID_ORDERBY_CONSTRUCT("Invalid order-by construct."),
        INVALID_COMPARISON("The comparision is not valid."),
        UNSUPPORTED_TYPE("The data type of the filter property is not supported.");

        private String message;

        ErrorCode(String aMessage) {
            message = aMessage;
        }

        public String getMessage() {
            return message;
        }
    }
}
