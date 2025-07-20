package com.dongpv.sns.identity.configuration;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.dongpv.sns.identity.constant.PredefinedRole;
import com.dongpv.sns.identity.entity.RoleEntity;
import com.dongpv.sns.identity.entity.UserEntity;
import com.dongpv.sns.identity.repository.RoleRepository;
import com.dongpv.sns.identity.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApplicationInitConfig {
    @NonFinal
    @Value("${master-data.admin-account.username}")
    String adminUsername;

    @NonFinal
    @Value("${master-data.admin-account.email}")
    String adminEmail;

    @NonFinal
    @Value("${master-data.admin-account.password}")
    String adminPassword;

    PasswordEncoder passwordEncoder;

    @Bean
    @ConditionalOnProperty(
            prefix = "spring",
            value = "datasource.driver-class-name",
            havingValue = "org.postgresql.Driver")
    ApplicationRunner applicationRunner(UserRepository userRepository, RoleRepository roleRepository) {
        return args -> {
            if (userRepository.findByEmail(adminEmail).isEmpty()) {
                RoleEntity userRole = RoleEntity.builder()
                        .name(PredefinedRole.USER_ROLE)
                        .description(PredefinedRole.USER_ROLE)
                        .build();
                roleRepository.save(userRole);

                RoleEntity adminRole = RoleEntity.builder()
                        .name(PredefinedRole.ADMIN_ROLE)
                        .description(PredefinedRole.ADMIN_ROLE)
                        .build();
                roleRepository.save(adminRole);

                var roles = new HashSet<RoleEntity>();
                roles.add(adminRole);
                UserEntity user = UserEntity.builder()
                        .username(adminUsername)
                        .email(adminEmail)
                        .emailVerified(true)
                        .password(passwordEncoder.encode(adminPassword))
                        .roles(roles)
                        .build();

                userRepository.save(user);
                LOGGER.warn("admin user has been created with default password, please change it");
            }
        };
    }
}
