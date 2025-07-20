package com.dongpv.sns.identity.util;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.util.StringUtils;

import com.dongpv.sns.identity.dto.PageApiResponseDto;
import com.dongpv.sns.identity.dto.PageApiResponseWithSummaryDto;
import com.google.common.base.CaseFormat;

public final class PaginationUtils {

    public static final int DEFAULT_RECORD_PER_PAGE = 10;
    public static final int DEFAULT_CURRENT_PAGE = 0;

    public static final String DEFAULT_SORT_PROPERTY = "id";
    public static final String SORT_COLUMN_PROPERTY = "sortColumn";
    public static final String SORT_TYPE_PROPERTY = "sortType";
    public static final String ASC = "asc";

    private PaginationUtils() {}

    public static PageRequest generatePageRequest(
            final String currentPage, final String pageSize, final String sortColumn, final String sortType) {
        String[] properties = parseProperties(sortColumn);
        Direction direction = ASC.equalsIgnoreCase(sortType) ? Direction.ASC : Direction.DESC;
        return PageRequest.of(getPage(currentPage), getSize(pageSize), direction, properties);
    }

    public static PageRequest generatePageRequest(final String currentPage, final String pageSize) {
        return PageRequest.of(getPage(currentPage), getSize(pageSize));
    }

    public static PageRequest generatePageRequest(final Map<String, String> params) {
        String sortBy =
                params.get(SORT_COLUMN_PROPERTY) == null ? DEFAULT_SORT_PROPERTY : params.get(SORT_COLUMN_PROPERTY);
        String sortType = params.get(SORT_TYPE_PROPERTY) == null ? ASC : params.get(SORT_TYPE_PROPERTY);
        if (params.get("isAll") != null && Boolean.parseBoolean(params.get("isAll"))) {
            return generatePageRequest(
                    String.valueOf(DEFAULT_CURRENT_PAGE), String.valueOf(Integer.MAX_VALUE), sortBy, sortType);
        }
        String page = params.get("page") == null ? String.valueOf(DEFAULT_CURRENT_PAGE) : params.get("page");
        String limit = params.get("limit") == null ? String.valueOf(DEFAULT_RECORD_PER_PAGE) : params.get("limit");
        return generatePageRequest(page, limit, sortBy, sortType);
    }

    public static <T> PageApiResponseDto<T> buildPageRes(final Page<T> page) {
        PageApiResponseDto<T> res = new PageApiResponseDto<>();
        res.setRecords(page.getContent());
        res.setPage(page.getNumber() + 1);
        res.setLimit(page.getSize());
        res.setTotal(page.getTotalElements());
        res.setTotalPage(page.getTotalPages());
        res.setHasNext(page.hasNext());
        return res;
    }

    public static <T, S> PageApiResponseWithSummaryDto<T, S> buildPageResWithSummaryData(
            final Page<T> page, final S summaryData) {
        PageApiResponseWithSummaryDto<T, S> res = new PageApiResponseWithSummaryDto<>();
        res.setRecords(page.getContent());
        res.setPage(page.getNumber() + 1);
        res.setLimit(page.getSize());
        res.setTotal(page.getTotalElements());
        res.setTotalPage(page.getTotalPages());
        res.setHasNext(page.hasNext());
        res.setSummary(summaryData);
        return res;
    }

    public static <T> PageApiResponseDto<T> buildPageResWithoutPagination(final List<T> records) {
        PageApiResponseDto<T> res = new PageApiResponseDto<>();
        res.setRecords(records);
        return res;
    }

    public static <T> Integer getPrev(final Page<T> page) {
        return page.hasPrevious() ? page.getNumber() - 1 : null;
    }

    public static <T> Integer getNext(final Page<T> page) {
        return page.hasNext() ? page.getNumber() + 1 : null;
    }

    private static Integer getPage(final String currentPage) {
        if (!StringUtils.hasText(currentPage)) {
            return DEFAULT_CURRENT_PAGE;
        }

        int page;
        try {
            page = Integer.parseInt(currentPage);
            page = page > 0 ? page - 1 : DEFAULT_CURRENT_PAGE;
        } catch (NumberFormatException e) {
            page = DEFAULT_CURRENT_PAGE;
        }

        return page;
    }

    private static Integer getSize(final String pageSize) {
        if (!StringUtils.hasText(pageSize)) {
            return DEFAULT_RECORD_PER_PAGE;
        }

        int size;
        try {
            size = Integer.parseInt(pageSize);
        } catch (NumberFormatException e) {
            size = DEFAULT_RECORD_PER_PAGE;
        }

        return size;
    }

    private static String[] parseProperties(final String sortColumn) {
        return Optional.ofNullable(sortColumn)
                .map(c -> Arrays.stream(StringUtils.commaDelimitedListToStringArray(c))
                        .map(PaginationUtils::underscoreToUppercase)
                        .toArray(String[]::new))
                .orElse(new String[] {DEFAULT_SORT_PROPERTY});
    }

    private static String underscoreToUppercase(final String value) {
        if (StringUtils.hasText(value) && value.contains("_")) {
            String upper = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, value);
            return upper.isEmpty() ? Strings.EMPTY : Character.toLowerCase(upper.charAt(0)) + upper.substring(1);
        }

        return value;
    }
}
