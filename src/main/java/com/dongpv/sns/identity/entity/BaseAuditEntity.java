package com.dongpv.sns.identity.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import org.apache.logging.log4j.util.Strings;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.dongpv.sns.identity.code.DeletedFlagType;
import com.dongpv.sns.identity.security.SecurityUtils;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@MappedSuperclass
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class BaseAuditEntity implements Serializable {
    @CreationTimestamp
    Instant createdAt;

    String createdBy;

    @UpdateTimestamp
    Instant updatedAt;

    String updatedBy;

    Integer isDeleted;

    Instant deletedAt;

    String deletedBy;

    @PrePersist
    public void onPrePersist() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.isDeleted = DeletedFlagType.NO.getCode();
        this.createdBy = SecurityUtils.getCurrentUserId() != null ? SecurityUtils.getCurrentUserId() : createdBy;
        this.updatedBy = SecurityUtils.getCurrentUserId() != null ? SecurityUtils.getCurrentUserId() : updatedBy;
        if (this.createdBy == null) {
            this.createdBy = Strings.EMPTY;
        }
        if (this.updatedBy == null) {
            this.updatedBy = Strings.EMPTY;
        }
    }

    @PreUpdate
    public void onPreUpdate() {
        this.updatedAt = Instant.now();
        this.updatedBy = SecurityUtils.getCurrentUserId() != null ? SecurityUtils.getCurrentUserId() : updatedBy;
        if (this.updatedBy == null) {
            this.updatedBy = Strings.EMPTY;
        }
    }

    public void softDelete() {
        this.deletedAt = Instant.now();
        this.deletedBy = SecurityUtils.getCurrentUserId() != null ? SecurityUtils.getCurrentUserId() : Strings.EMPTY;
        this.isDeleted = DeletedFlagType.YES.getCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        BaseAuditEntity that = (BaseAuditEntity) o;
        return Objects.equals(createdBy, that.createdBy)
                && Objects.equals(updatedBy, that.updatedBy)
                && Objects.equals(createdAt, that.createdAt)
                && Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), createdBy, updatedBy, createdAt, updatedAt);
    }

    @Override
    public String toString() {
        return "BaseAuditEntity{"
                + "createdBy='"
                + createdBy
                + '\''
                + ", updatedBy='"
                + updatedBy
                + '\''
                + ", createdAt="
                + createdAt
                + ", updatedAt="
                + updatedAt
                + "} "
                + super.toString();
    }
}
