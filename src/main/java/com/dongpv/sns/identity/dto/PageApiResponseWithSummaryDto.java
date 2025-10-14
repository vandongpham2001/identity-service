package com.dongpv.sns.identity.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@JsonRootName("data")
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = true)
public class PageApiResponseWithSummaryDto<T, S> extends PageApiResponseDto<T> {
    private S summary;
}
