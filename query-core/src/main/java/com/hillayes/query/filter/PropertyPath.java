package com.hillayes.query.filter;

/**
 * Represents the path to a property in a class. The path is derived from
 * a dot-delimited string of property names.
 */
public final class PropertyPath {
    private final String name;
    private final PropertyPath child;

    /**
     * Parses the given dot-delimited path into a PropertyPath instance.
     * @param path the dot-delimited path to a property.
     * @return the PropertyPath instance.
     */
    public static PropertyPath of(String path) {
        int index = path.indexOf('.');
        if (index > 0) {
            return new PropertyPath(path.substring(0, index), PropertyPath.of(path.substring(index + 1)));
        }
        return new PropertyPath(path, null);
    }

    /**
     * Constructs a PropertyPath instance with the given name and child.
     * @param name the name of the property.
     * @param child the child property.
     */
    private PropertyPath(String name, PropertyPath child) {
        this.name = name;
        this.child = child;
    }

    /**
     * Returns the name of the root property referenced in this path.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns true if this path has a child property.
     */
    public boolean hasChild() {
        return child != null;
    }

    /**
     * Returns the child property path.
     */
    public PropertyPath getChild() {
        return child;
    }

    public String toString() {
        return name + (child == null ? "" : "." + child);
    }
}
