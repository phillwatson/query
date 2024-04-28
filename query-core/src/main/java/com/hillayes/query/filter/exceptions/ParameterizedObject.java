/*
 * Copyright 2017-2018 Phillip Watson
 */
package com.hillayes.query.filter.exceptions;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * An object that has the ability to record parameters that provide a clue as to the context in
 * which the object occurred.
 *
 * @author pwatson
 */
public class ParameterizedObject {
    private static final Map<String, Object> NULL_MAP = Collections.emptyMap();

    private Map<String, Object> parameters = ParameterizedObject.NULL_MAP;

    public boolean isEmpty() {
        return parameters.isEmpty();
    }

    public Set<Entry<String, Object>> entrySet() {
        return parameters.entrySet();
    }

    /**
     * Sets a named attribute that adds contextual information to the object. The given value will
     * overwrite any existing value of the same name.
     *
     * @param aName  the name of the attribute.
     * @param aValue the value of the attribue.
     */
    public final ParameterizedObject set(String aName, Object aValue) {
        if (aName != null) {
            if (aValue != null) {
                if (parameters == ParameterizedObject.NULL_MAP) {
                    parameters = new HashMap<>(2);
                }
                parameters.put(aName, aValue);
            } else if (parameters != ParameterizedObject.NULL_MAP) {
                parameters.remove(aName);
            }
        }

        return this;
    }

    /**
     * Sets a named attribute that adds contextual information to the object. The given value will
     * overwrite any existing value of the same name.
     *
     * @param aName  the name of the attribute.
     * @param aValue the value of the attribue.
     */
    public final ParameterizedObject set(String aName, int aValue) {
        return set(aName, Integer.valueOf(aValue));
    }

    /**
     * Sets a named attribute that adds contextual information to the object. The given value will
     * overwrite any existing value of the same name.
     *
     * @param aName  the name of the attribute.
     * @param aValue the value of the attribue.
     */
    public final ParameterizedObject set(String aName, long aValue) {
        return set(aName, Long.valueOf(aValue));
    }

    /**
     * Sets a named attribute that adds contextual information to the object. The given value will
     * overwrite any existing value of the same name.
     *
     * @param aName  the name of the attribute.
     * @param aValue the value of the attribue.
     */
    public final ParameterizedObject set(String aName, boolean aValue) {
        return set(aName, Boolean.valueOf(aValue));
    }

    /**
     * Returns all available contextual parameter keys. If there are no parameters for this object,
     * the return value is an empty Set. The set is not modifiable, and attempt to modify its
     * content will raise an UnsupportedOperationException.
     *
     * @return the Map of all contextual parameter values, keyed on their name.
     */
    public final Set<String> getKeys() {
        return Collections.unmodifiableSet(parameters.keySet());
    }

    /**
     * Returns the number of contextual parameters that this object holds.
     *
     * @return the number of context parameters.
     */
    public final int getCount() {
        return parameters.size();
    }

    /**
     * Returns the value of the named parameter. If no parameter of the given name exists, the
     * return value will be <code>null</code>.
     *
     * @param <T>   the type to which the parameter will be cast.
     * @param aName the name of the parameter.
     * @return the parameter value, or <code>null</code>.
     */
    @SuppressWarnings("unchecked")
    public final <T> T get(String aName) {
        Object result = parameters.get(aName);
        return ((T) result);
    }
}
