package com.dongpv.sns.identity.constant;

public class RouteConstant {
    public static final String ADMIN = "/admin";

    public static class Admin {
        public static final String ROLES = ADMIN + "/roles";
        public static final String PERMISSIONS = ADMIN + "/permissions";
        public static final String USERS = ADMIN + "/users";

        private Admin() {}
    }

    public static class User {
        public static final String AUTH = "/auth";

        private User() {}
    }

    private RouteConstant() {}
}
