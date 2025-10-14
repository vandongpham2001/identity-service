package com.dongpv.sns.identity.constant;

public class RouteConstant {
    public static final String ADMIN = "/admin";

    public static class Admin {
        public static final String ROLE = ADMIN + "/role";
        public static final String PERMISSION = ADMIN + "/permission";
        public static final String USER = ADMIN + "/user";

        private Admin() {}
    }

    public static final String AUTH = "/auth";

    private RouteConstant() {}
}
