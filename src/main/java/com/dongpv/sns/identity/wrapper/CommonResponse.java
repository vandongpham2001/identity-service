package com.dongpv.sns.identity.wrapper;

public record CommonResponse<T>(T data, String message) {}
