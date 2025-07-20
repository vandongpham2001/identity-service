package com.dongpv.sns.identity.entity.listener;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import com.dongpv.sns.identity.entity.UserEntity;

public class UserEntityListener {
    @PrePersist
    @PreUpdate
    public void onPrepare(UserEntity user) {
        if (user.getUsername() != null) {
            user.setUsername(user.getUsername().toLowerCase());
        }

        if (user.getEmail() != null) {
            user.setEmail(user.getEmail().toLowerCase());
        }
    }
}
