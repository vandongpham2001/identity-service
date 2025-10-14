package com.dongpv.sns.identity.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import org.springframework.stereotype.Component;

@Component
public class DateMapper {

    public LocalDateTime instantToLocalDateTime(Instant instant) {
        return instant.atZone(ZoneId.of("UTC")).toLocalDateTime();
    }

    public Instant localDateTimeToInstant(LocalDateTime localDateTime) {
        return localDateTime.toInstant(ZoneOffset.UTC);
    }
}
