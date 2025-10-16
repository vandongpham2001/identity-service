package com.dongpv.sns.identity.dto.request.admin;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseFilterRequestDto {
    @Builder.Default
    private String keyword = "";

    @Builder.Default
    private String sortColumn = "createdAt";

    @Builder.Default
    private String sortType = "desc";
}
