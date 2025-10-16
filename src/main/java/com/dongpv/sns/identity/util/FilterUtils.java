package com.dongpv.sns.identity.util;

import java.util.Map;

import com.dongpv.sns.identity.dto.request.admin.BaseFilterRequestDto;

public final class FilterUtils {
    private FilterUtils() {}

    public static BaseFilterRequestDto handleFilterRequest(
            final Map<String, String> requestParams, boolean useRawQuery) {
        var filter = new BaseFilterRequestDto();

        filter.setKeyword(StringUtils.getValue(requestParams.get("keyword")));

        if (!StringUtils.getValue(requestParams.get("sortColumn")).isEmpty()) {
            filter.setSortColumn(StringUtils.getValue(requestParams.get("sortColumn")));
        }

        if (!StringUtils.getValue(requestParams.get("sortType")).isEmpty()) {
            filter.setSortType(StringUtils.getValue(requestParams.get("sortType")));
        }

        if (useRawQuery) {
            filter.setSortColumn(StringUtils.camelToSnake(filter.getSortColumn()));
        }

        return filter;
    }
}
