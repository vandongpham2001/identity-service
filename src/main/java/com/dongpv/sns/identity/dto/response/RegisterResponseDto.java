package com.dongpv.sns.identity.dto.response;

import java.time.LocalDate;

import com.dongpv.sns.identity.code.Gender;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterResponseDto {
    String email;
    String firstName;
    String lastName;
    LocalDate dob;
    Gender gender;
}
