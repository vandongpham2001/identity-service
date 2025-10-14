package com.dongpv.sns.identity.dto;

import java.io.Serializable;

public record FileAttachment(String fileName, String filePath, String contentType) implements Serializable {}
