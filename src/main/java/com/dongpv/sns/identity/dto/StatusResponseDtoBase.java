package com.dongpv.sns.identity.dto;

import jakarta.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author DongPV
 */
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class StatusResponseDtoBase implements BaseApiResponse {

    /** */
    @NotEmpty
    @Length(min = 1, max = 32)
    @JsonProperty("STATUS")
    private String status;

    /**
     * @param status
     */
    public StatusResponseDtoBase(STATUS status) {
        this.status = status.label;
    }

    @Getter
    public enum STATUS {
        TRUE("True"),
        FALSE("False");

        /** */
        private String label;

        /**
         * @param label
         */
        STATUS(String label) {
            this.label = label;
        }
    }
}
