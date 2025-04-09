package com.dongpv.sns.identity.code;

public enum DeletedFlagType implements DisplayCodesInt {
    NO(0, "No"),
    YES(1, "Yes");

    private final Integer code;

    private final String display;

    /**
     * @param code
     * @param display
     */
    DeletedFlagType(Integer code, String display) {
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
