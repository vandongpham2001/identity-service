package com.dongpv.sns.identity.sort;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseEntitySortConfig {
    public static final String ID = "id";
    public static final String CREATED_AT = "created_at";
    public static final String UPDATED_AT = "updated_at";

    public List<String> getCommonSortableColumns() {
        return List.of(
                ID,
                CREATED_AT,
                UPDATED_AT
        );
    }

    public abstract List<String> getEntitySpecificSortableColumns();

    public List<String> getSortableColumns() {
        List<String> columns = new ArrayList<>(getCommonSortableColumns());
        columns.addAll(getEntitySpecificSortableColumns());
        return columns;
    }
}
