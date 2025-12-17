package me.nickhanson.codeforge.evaluator;

import java.util.Locale;

/**
 * Utility class for normalizing strings for evaluation.
 * Provides methods to standardize strings by trimming whitespace,
 * converting to lowercase, collapsing multiple spaces, and stripping punctuation.
 * @author Nick Hanson
 */
public final class Normalizer {
    private Normalizer() {}

    /**
     * Performs basic normalization on the input string:
     * - Trims leading and trailing whitespace
     * - Converts to lowercase
     * - Collapses multiple whitespace characters into a single space
     * @param s The input string to normalize
     * @return The normalized string
     */
    public static String basic(String s) {
        if (s == null) return null;
        String trimmed = s.trim().toLowerCase(Locale.ROOT);
        return collapseWhitespace(trimmed);
    }

    /**
     * Collapses multiple consecutive whitespace characters into a single space.
     * @param s The input string
     * @return The string with collapsed whitespace
     */
    public static String collapseWhitespace(String s) {
        return s.replaceAll("\\s+", " ");
    }

    /**
     * Strips all punctuation characters from the input string.
     * @param s The input string
     * @return The string without punctuation
     */
    public static String stripPunctuation(String s) {
        if (s == null) return null;
        return s.replaceAll("[\\p{Punct}]", "");
    }
}

