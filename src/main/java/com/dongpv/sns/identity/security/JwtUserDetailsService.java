package com.dongpv.sns.identity.security;

import static com.dongpv.sns.identity.constant.CommonConstant.ROLE_PREFIX;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.dongpv.sns.identity.exception.UserNotFoundException;
import com.dongpv.sns.identity.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtUserDetailsService implements UserDetailsService {
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UserNotFoundException {
        var user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        Set<GrantedAuthority> authorities = new HashSet<>();

        for (var role : user.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(ROLE_PREFIX + role.getName()));
            for (var permission : role.getPermissions()) {
                authorities.add(new SimpleGrantedAuthority(permission.getName()));
            }
        }

        return new JwtUserDetails(
                user.getEmail(), user.getPassword(), authorities, user.getId(), user.getEmail(), user.getUsername());
    }
}
