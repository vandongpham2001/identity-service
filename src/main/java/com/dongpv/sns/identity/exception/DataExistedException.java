package com.dongpv.sns.identity.exception;

import java.io.Serial;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

public class DataExistedException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;
    private static final String FIELD_NAME = "field_name";
    private static final String FIELD_VALUE = "field_value";
    private static final String TYPE = "type";
    private static final String UNIQUE = "unique";

    public DataExistedException(String fieldName, String fieldValue) {
        super(getMessage(fieldName, fieldValue));
    }

    private static String getMessage(String fieldName, String fieldValue) {
        Map<String, String> map = new HashMap<String, String>();
        map.put(FIELD_NAME, fieldName);
        map.put(FIELD_VALUE, fieldValue);
        map.put(TYPE, UNIQUE);
        return new JSONObject(map).toString();
    }
}
