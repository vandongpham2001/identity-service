package com.dongpv.sns.identity.util;

import java.util.function.Function;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ParserUtils {
    public static <T> T tryParse(String value, Function<String, T> parser) {
        try {
            return parser.apply(value);
        } catch (NumberFormatException ex) {
            LOGGER.error("Failed to parse value: '{}'", value, ex);
            return null;
        }
    }

    public static <T> T tryParse(String value, Function<String, T> parser, T defaultValue) {
        try {
            return parser.apply(value);
        } catch (NumberFormatException ex) {
            LOGGER.error("Failed to parse value: '{}'", value, ex);
            return defaultValue;
        }
    }
}
