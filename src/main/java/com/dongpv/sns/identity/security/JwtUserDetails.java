package com.dongpv.sns.identity.security;

import java.util.Collection;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtUserDetails extends User {
    String id;
    String email;
    String actualUsername;

    public JwtUserDetails(
            String username,
            String password,
            Collection<? extends GrantedAuthority> authorities,
            String id,
            String email,
            String actualUsername) {
        super(username, password, authorities);
        this.id = id;
        this.email = email;
        this.actualUsername = actualUsername;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        JwtUserDetails that = (JwtUserDetails) o;
        return Objects.equals(id, that.id)
                && Objects.equals(email, that.email)
                && Objects.equals(actualUsername, that.actualUsername);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, email, actualUsername);
    }
}
