package com.hillayes.query.filter.exceptions;

public  enum ErrorCode {
    INVALID_PROPERTY_REF("The named property is not available to filter expressions."),
    INVALID_FILTER_EXPRESSION("Invalid filter expression."),
    INVALID_ORDERBY_COL("The named property is not available to the order-by clause."),
    INVALID_ORDERBY_CONSTRUCT("Invalid order-by construct."),
    INVALID_EXPRESSION("The expression is not valid."),
    UNSUPPORTED_TYPE("The data type of the filter property is not supported.");

    private final String message;

    ErrorCode(String aMessage) {
        message = aMessage;
    }

    public String getMessage() {
        return message;
    }
}
