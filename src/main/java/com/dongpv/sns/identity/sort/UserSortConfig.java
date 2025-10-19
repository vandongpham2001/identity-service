package com.dongpv.sns.identity.sort;

import java.util.List;

public class UserSortConfig extends BaseEntitySortConfig {
    public static final String EMAIL = "email";
    public static final String USERNAME = "username";

    @Override
    public List<String> getEntitySpecificSortableColumns() {
        return List.of(EMAIL, USERNAME);
    }
}
