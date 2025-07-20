package com.dongpv.sns.identity.util;

import java.io.Serial;
import java.util.Objects;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DebugInfo extends RuntimeException {

    /** */
    @Serial
    private static final long serialVersionUID = 1637061345647991355L;

    /** */
    private final String message;

    /**
     * @param message
     */
    public DebugInfo(String message) {
        this.message = message;
    }

    /** */
    public void log() {

        if (LOGGER.isDebugEnabled()) {
            logInfo(this.message);
        }
    }

    /**
     * @param message
     */
    private void logInfo(String message) {
        // get class and method name
        final StackTraceElement trace = getStackTrace()[0];
        final String name = trace.getClassName();
        final String className = name.substring(name.lastIndexOf('.') + 1, name.length());
        final String methodName = trace.getMethodName();

        // if there is no message, we give the line number instead
        message = Objects.isNull(message) ? Integer.toString(trace.getLineNumber()) : message;

        LOGGER.info("{}::{}() - {}", className, methodName, message);
    }
}
