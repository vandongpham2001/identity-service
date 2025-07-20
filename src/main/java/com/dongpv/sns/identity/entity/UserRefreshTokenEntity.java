package com.dongpv.sns.identity.entity;

import java.time.Instant;
import java.util.Objects;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "refresh_tokens")
public class UserRefreshTokenEntity extends BaseEntity {
    @Column(name = "token_hash", nullable = false, unique = true)
    String hashedToken;

    @Column(name = "expiry_date", nullable = false)
    Instant expiryDate;

    @Column(name = "email", nullable = false)
    String email;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        UserRefreshTokenEntity that = (UserRefreshTokenEntity) o;
        return Objects.equals(hashedToken, that.hashedToken)
                && Objects.equals(expiryDate, that.expiryDate)
                && Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), hashedToken, expiryDate, email);
    }
}
