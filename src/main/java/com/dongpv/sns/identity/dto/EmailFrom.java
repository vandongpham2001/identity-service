package com.dongpv.sns.identity.dto;

public record EmailFrom(String service, String mail, String token, Long userId) {}
