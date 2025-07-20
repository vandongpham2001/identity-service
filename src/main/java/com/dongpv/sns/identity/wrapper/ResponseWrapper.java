package com.dongpv.sns.identity.wrapper;

import java.util.HashMap;

public record ResponseWrapper<T>(T data, HashMap<String, String> detail) {
    public ResponseWrapper(T data) {
        this(data, new HashMap<>());
    }
}
