package com.dongpv.sns.identity.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import jakarta.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmailNotification implements Serializable {
    @JsonProperty("from")
    private transient @Nullable EmailFrom from;

    @JsonProperty("emailTo")
    private List<String> emailTo;

    @JsonProperty("subjectParams")
    private transient HashMap<String, Object> subjectParams = new HashMap<>();

    @JsonProperty("data")
    private transient HashMap<String, Object> data = new HashMap<>();

    @JsonProperty("type")
    private String type;

    @JsonProperty("userId")
    private String userId;
}
