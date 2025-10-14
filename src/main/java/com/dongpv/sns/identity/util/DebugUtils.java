package com.dongpv.sns.identity.util;

import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author DongPV
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DebugUtils {

    /** */
    private static final ObjectMapper mapper =
            Jackson2ObjectMapperBuilder.json().build();

    /** {calling class}:{line number}:{calling method}() - \n{object name}=\n{object} */
    private static final String LOG_JSON = "{}:{}:{}() - \n{}={}";

    private static final String LOG_STRING = "{}:{}:{}() - {}={}";

    /**
     * Log object as JSON format.
     *
     * @param objName
     * @param obj
     */
    public static void logAsJson(String objName, Object obj) {
        log(objName, obj, true);
    }

    /**
     * Log object as toString() format.
     *
     * @param objName
     * @param obj
     */
    public static void log(String objName, Object obj) {
        log(objName, obj, false);
    }

    /**
     * Log object.
     *
     * @param objName
     * @param obj
     */
    private static void log(String objName, Object obj, boolean asJson) {

        // check for debug logging because this is expensive
        if (LOGGER.isDebugEnabled()) {
            final StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            final String[] split = stackTraceElement.getClassName().split("\\.");

            final String callingClass = split[split.length - 1];
            final int lineNumber = stackTraceElement.getLineNumber();
            final String callingMethod = stackTraceElement.getMethodName();

            if (asJson) {
                LOGGER.debug(LOG_JSON, callingClass, lineNumber, callingMethod, objName, toJsonString(obj));
            } else {
                LOGGER.debug(LOG_STRING, callingClass, lineNumber, callingMethod, objName, obj);
            }
        }
    }

    /**
     * Attempt to convert to JSON string; otherwise toString() if not null.
     *
     * @param source
     * @return JSON string, otherwise toString() if not null
     */
    private static String toJsonString(Object source) {

        if (source != null) {

            try {
                // attempt to convert to JSON
                return mapper.writeValueAsString(source);

            } catch (final JsonProcessingException ex) {
                // just log warning
                LOGGER.warn(ex.getLocalizedMessage(), ex);
            }

            // fallback to toString()
            return source.toString();
        }

        return null;
    }
}
