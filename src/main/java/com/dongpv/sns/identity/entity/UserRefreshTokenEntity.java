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

    @Column(name = "expired_at", nullable = false)
    Instant expiredAt;

    @Column(name = "user_id", nullable = false)
    String userId;

    @Column(name = "email", nullable = false)
    String email;

    @Column(name = "device_info")
    String deviceInfo;

    @Builder.Default
    @Column(name = "revoked")
    Boolean revoked = false;

    @Column(name = "replaced_by")
    String replacedBy;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        UserRefreshTokenEntity that = (UserRefreshTokenEntity) o;
        return Objects.equals(hashedToken, that.hashedToken)
                && Objects.equals(expiredAt, that.expiredAt)
                && Objects.equals(email, that.email)
                && Objects.equals(userId, that.userId)
                && Objects.equals(deviceInfo, that.deviceInfo)
                && Objects.equals(revoked, that.revoked)
                && Objects.equals(replacedBy, that.replacedBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), hashedToken, expiredAt, email, userId, deviceInfo, revoked, replacedBy);
    }
}
