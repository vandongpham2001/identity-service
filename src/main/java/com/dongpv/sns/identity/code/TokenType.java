package com.dongpv.sns.identity.code;

public enum TokenType implements DisplayCodesInt {
    ACCESS_TOKEN(0, "Access Token"),
    REFRESH_TOKEN(1, "Refresh Token"),;

    private final Integer code;

    private final String display;

    /**
     * @param code
     * @param display
     */
    TokenType(Integer code, String display) {
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
