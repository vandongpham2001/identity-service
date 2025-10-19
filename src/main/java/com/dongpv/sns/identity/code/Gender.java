package com.dongpv.sns.identity.code;

public enum Gender implements DisplayCodesInt {
    FEMALE(0, "Female"),
    MALE(1, "Male");

    private final Integer code;

    private final String display;

    Gender(Integer code, String display) {
        this.code = code;
        this.display = display;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getDisplay() {
        return display;
    }
}
