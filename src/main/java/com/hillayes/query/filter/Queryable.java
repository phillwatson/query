package com.hillayes.query.filter;

/**
 * A shared interface for classes and properties that can be referenced in a
 * query expression.
 */
public interface Queryable {
    /**
     * Returns the nested property identified by the given dot-delimited path.
     * The search will be recursive, so that a.b.c will return the property
     * c of the property b of the property a.
     * @param path the dot-delimited path to the desired property.
     * @return the property identified by the given path, or <code>null</code> if not found.
     */
    public default QueryProperty getProperty(String path) {
        return getProperty(PropertyPath.of(path));
    }

    /**
     * Returns the nested property identified by the given path. The search will
     * be recursive, so that a.b.c will return the property c of the property b
     * of the property a.
     * @param aPath the path to the desired property.
     * @return the property identified by the given path, or <code>null</code> if not found.
     */
    public default QueryProperty getProperty(PropertyPath aPath) {
        return null;
    }
}
