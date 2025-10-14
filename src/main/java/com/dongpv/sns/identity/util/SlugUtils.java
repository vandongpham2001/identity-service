package com.dongpv.sns.identity.util;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SlugUtils {
    private static final Pattern nonLatin = Pattern.compile("[^\\p{IsAlphabetic}^\\p{IsDigit}-]");
    private static final Pattern whiteSpace = Pattern.compile("\\s");
    private static final Pattern nonInitial = Pattern.compile("^[^\\p{IsAlphabetic}^\\p{IsDigit}]+");
    private static final Pattern nonAlpha = Pattern.compile("[^\\p{IsAlphabetic}^\\p{IsDigit}]+$");

    public static String toSlug(String input) {
        input = nonInitial.matcher(input).replaceAll("");
        input = nonAlpha.matcher(input).replaceAll("");
        String noWhiteSpace = whiteSpace.matcher(input).replaceAll("-");
        String normalized = Normalizer.normalize(noWhiteSpace, Normalizer.Form.NFD);
        String slug = nonLatin.matcher(normalized).replaceAll("");
        return slug.toLowerCase(Locale.ENGLISH);
    }
}
