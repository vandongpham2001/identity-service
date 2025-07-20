package com.dongpv.sns.identity.dto;

import java.io.Serializable;
import java.util.List;

import jakarta.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SendMessageDto implements Serializable {
    @JsonProperty("recipients")
    private List<Long> recipients;

    @JsonProperty("sender")
    Long sender;

    @JsonProperty("content")
    @Nullable
    String content;
}
