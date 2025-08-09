package com.dongpv.sns.identity.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/test")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TestController {
    @GetMapping
    public String test() {
        LOGGER.info("Test Log Info");
        LOGGER.warn("Test Log Warn");
        LOGGER.error("Test Log Error");
        return "Hello World";
    }
}
