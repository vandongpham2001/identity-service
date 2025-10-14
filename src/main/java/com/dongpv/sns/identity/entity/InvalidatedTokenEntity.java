package com.dongpv.sns.identity.entity;

import java.util.Date;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "invalidated_tokens")
public class InvalidatedTokenEntity extends BaseAuditEntity {
    @Id
    String id;

    Date expiryTime;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        InvalidatedTokenEntity that = (InvalidatedTokenEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(expiryTime, that.expiryTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, expiryTime);
    }
}
