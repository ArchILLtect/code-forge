package me.nickhanson.codeforge.evaluator;

import java.util.Locale;

public final class Normalizer {
    private Normalizer() {}

    public static String basic(String s) {
        if (s == null) return null;
        String trimmed = s.trim().toLowerCase(Locale.ROOT);
        return collapseWhitespace(trimmed);
    }

    public static String collapseWhitespace(String s) {
        return s.replaceAll("\\s+", " ");
    }

    public static String stripPunctuation(String s) {
        if (s == null) return null;
        return s.replaceAll("[\\p{Punct}]", "");
    }
}

