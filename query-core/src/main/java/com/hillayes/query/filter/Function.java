package com.hillayes.query.filter;

public interface Function {
    default String formatValue(String aValue) {
        return aValue;
    }

    void appendTo(StringBuilder aBuilder, String aColumnName);
}
