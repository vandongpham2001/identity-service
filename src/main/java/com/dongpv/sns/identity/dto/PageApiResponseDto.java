package com.dongpv.sns.identity.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;

import lombok.Data;

@Data
@JsonRootName("data")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageApiResponseDto<T> {
    private Long total;
    private Integer limit;
    private Integer totalPage;
    private Integer page;
    private Boolean hasNext;
    private List<T> records;
}
