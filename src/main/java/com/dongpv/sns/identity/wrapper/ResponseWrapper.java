package com.dongpv.sns.identity.wrapper;

import java.util.HashMap;
import java.util.Map;

public record ResponseWrapper<T>(T data, Map<String, String> detail) {
    public ResponseWrapper(T data) {
        this(data, new HashMap<>());
    }
}
