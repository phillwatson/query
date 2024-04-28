/*
 * Copyright 2017-2018 Phillip Watson
 */
package com.hillayes.query.filter.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * A collection of utility methods related to String objects.
 *
 * @author pwatson
 */
public final class Strings {
    private Strings() {
    }

    /**
     * The character encoding to be used for the data. References the UTF-8 encoding. Should be used when calling
     * {@link String#getBytes()}.
     */
    public static final Charset DEFAULT_ENCODING = StandardCharsets.UTF_8;

    /**
     * A null-safe, lexicographical comparison of two strings. The comparison is based on the Unicode value of each
     * character in the strings. The character sequence represented by String a1 is compared lexicographically to the
     * character sequence represented by the String a2.
     *
     * @param a1 the first String to be compared.
     * @param a2 the second String to be compared.
     * @return the value 0 if the argument string is equal to this string; a value less than 0 if this string is
     * lexicographically less than the string argument; and a value greater than 0 if this string is
     * lexicographically greater than the string argument
     */
    public static int compare(String a1, String a2) {
        if (a1 == a2) {
            return 0;
        }

        return (a1 == null) ? -1 : (a2 == null) ? 1 : a1.compareTo(a2);
    }

    /**
     * Compares the two Strings for equality regardless of case, and in a <code>null</code>-safe manner.
     * <p>
     * If both Strings are <code>null</code>, the result is <code>true</code>. If only one String is <code>null</code>,
     * the result is <code>false</code>. Otherwise, String <code>B</code> is passed to the equals() method of String
     * <code>A</code>, and the result returned.
     *
     * @param a1 the first String to be compared.
     * @param a2 the second String to be compared.
     * @return <code>true</code> if both Strings are <code>null</code>, or are equal according to the
     * <code>equals()</code> method of String <code>A</code>.
     */
    public static boolean equalsIgnoreCase(String a1, String a2) {
        if (a1 == a2) {
            return (true);
        }
        return (a1 != null) && (a1.equalsIgnoreCase(a2));
    }

    /**
     * Compares the two Strings for equality in a <code>null</code>-safe manner.
     * <p>
     * If both Strings are <code>null</code>, the result is <code>true</code>. If only one String is <code>null</code>,
     * the result is <code>false</code>. Otherwise, String <code>B</code> is passed to the equals() method of String
     * <code>A</code>, and the result returned.
     *
     * @param a1 the first String to be compared.
     * @param a2 the second String to be compared.
     * @return <code>true</code> if both Strings are <code>null</code>, or are equal according to the
     * <code>equals()</code> method of String <code>A</code>.
     */
    public static boolean equals(String a1, String a2) {
        return (a1 != null) && (a1.equals(a2));
    }

    /**
     * Tests whether the given string is null, empty or contains only whitespace.
     *
     * @param aStr the string to be tested.
     * @return <code>true</code> if the string is considered to be empty.
     */
    public static boolean isEmpty(String aStr) {
        return ((aStr == null) || (aStr.isBlank()));
    }

    /**
     * Trim leading and trailing whitespace from the given string and then ensure the resulting length does not exceed the
     * given max value; by removing those characters after the max length. If the result is an empty string, the return
     * value will be <code>null</code>.
     *
     * @param aValue  the value to be trimmed.
     * @param aMaxLen the maximum length of the result.
     * @return the trimmed value.
     */
    public static String trim(String aValue, int aMaxLen) {
        if ((aValue == null) || (aMaxLen <= 0)) {
            // don't bother doing anything
            return null;
        }

        // remove trailing and leading whitespace
        String result = aValue.trim();

        // if the string is empty
        int len = result.length();
        if (len == 0) {
            return null;
        }

        // if the result so far exceeds max length
        if (len > aMaxLen) {
            // truncate result
            result = result.substring(0, aMaxLen);

            // we may have truncated on trailing whitespace - remove it
            // we won't have any leading whitespace at this point
            result = result.trim();
        }

        return result;
    }

    /**
     * Performs an efficient read of the given InputStream. Does not close the InputStream.
     *
     * @param aInput the InputStream to be read into the resulting String.
     * @return the String read from the given InputStream.
     * @throws IOException
     */
    public static String read(InputStream aInput) throws IOException {
        StringBuilder result = new StringBuilder();

        byte[] buffer = new byte[1028];
        int len;
        while ((len = aInput.read(buffer)) > 0) {
            result.append(new String(buffer, 0, len));
        }

        return result.toString();
    }
}
