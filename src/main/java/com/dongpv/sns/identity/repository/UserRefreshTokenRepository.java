package com.dongpv.sns.identity.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dongpv.sns.identity.entity.UserRefreshTokenEntity;

@Repository
public interface UserRefreshTokenRepository extends JpaRepository<UserRefreshTokenEntity, String> {
    Optional<UserRefreshTokenEntity> findByHashedToken(String token);
}
