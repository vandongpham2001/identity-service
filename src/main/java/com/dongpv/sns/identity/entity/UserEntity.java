package com.dongpv.sns.identity.entity;

import java.util.Objects;
import java.util.Set;

import jakarta.persistence.*;

import com.dongpv.sns.identity.entity.listener.UserEntityListener;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "users")
@EntityListeners(UserEntityListener.class)
public class UserEntity extends BaseEntity {
    @Column(name = "email", unique = true)
    String email;

    @Column(name = "username", unique = true)
    String username;

    String password;

    @Builder.Default
    @Column(name = "email_verified")
    Boolean emailVerified = false;

    @ManyToMany
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleEntity> roles;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        UserEntity that = (UserEntity) o;
        return Objects.equals(email, that.email)
                && Objects.equals(username, that.username)
                && Objects.equals(password, that.password)
                && Objects.equals(emailVerified, that.emailVerified)
                && Objects.equals(roles, that.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), email, username, password, emailVerified, roles);
    }
}
