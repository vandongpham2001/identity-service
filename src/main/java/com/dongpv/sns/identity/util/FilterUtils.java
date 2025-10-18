package com.dongpv.sns.identity.util;

import java.util.Map;
import java.util.Optional;

import com.dongpv.sns.identity.dto.request.admin.BaseFilterRequestDto;
import com.dongpv.sns.identity.sort.BaseEntitySortConfig;
import com.dongpv.sns.identity.sort.UserSortConfig;

public final class FilterUtils {
    private FilterUtils() {}

    public static BaseFilterRequestDto handleFilterRequest(
            final Map<String, String> requestParams, boolean useRawQuery) {
        var filter = new BaseFilterRequestDto();
        BaseEntitySortConfig sortConfig = new UserSortConfig();

        filter.setKeyword(StringUtils.getValue(requestParams.get("keyword")));

        String rawSortColumn = StringUtils.getValue(requestParams.get("sortColumn"));
        if (rawSortColumn.isEmpty()) {
            rawSortColumn = filter.getSortColumn();
        }

        if (useRawQuery) {
            rawSortColumn = StringUtils.camelToSnake(rawSortColumn);
        }

        if (!sortConfig.getSortableColumns().contains(rawSortColumn.toLowerCase())) {
            rawSortColumn = BaseEntitySortConfig.CREATED_AT;
        }
        filter.setSortColumn(rawSortColumn);

        String rawSortType = StringUtils.getValue(requestParams.get("sortType"));
        if (rawSortType.isEmpty()) {
            rawSortType = filter.getSortType();
        }
        filter.setSortType(rawSortType.equalsIgnoreCase(SortUtils.ASC) ? SortUtils.ASC : SortUtils.DESC);

        return filter;
    }
}
