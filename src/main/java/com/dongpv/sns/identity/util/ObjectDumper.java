package com.dongpv.sns.identity.util;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

import org.apache.logging.log4j.util.Strings;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ObjectDumper {

    /** */
    private static final ObjectMapper mapper =
            Jackson2ObjectMapperBuilder.json().build();

    static {
        if (!LOGGER.isDebugEnabled()) {
            mapper.setDefaultPrettyPrinter(new MinimalPrettyPrinter());
        }

        mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    /** */
    private ObjectDumper() {}

    /**
     * @param source
     * @return
     */
    public static final String toJsonString(Object source) {

        try {
            // take care of null case
            final Object target = Optional.ofNullable(source).orElse(Strings.EMPTY);
            // attempt to convert to JSON
            return mapper.writeValueAsString(target);

        } catch (final JsonProcessingException ex) {
            // just log warning
            LOGGER.warn(ex.getLocalizedMessage(), ex);
            LOGGER.info("{}", source);
        }

        return Strings.EMPTY;
    }

    /**
     * @param obj
     * @param filename
     * @return true if successful in writing to file
     */
    public static final boolean writeToFile(Object obj, String filename) {
        boolean success = false;

        final String outputFilename = Optional.ofNullable(filename).orElse("./test.json");

        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outputFilename))) {
            // attempt to write to file
            mapper.writeValue(bos, obj);
            success = true;

        } catch (final IOException ex) {
            // just log warning
            LOGGER.warn(ex.getLocalizedMessage(), ex);
        }

        return success;
    }

    /**
     * @param obj
     * @return
     */
    public static final boolean writeToFile(Object obj) {
        return writeToFile(obj, null);
    }
}
