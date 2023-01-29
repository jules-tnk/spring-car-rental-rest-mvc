package io.bootify.spring_car_rental.service.security;

import io.bootify.spring_car_rental.domain.user_management.AppUser;
import io.bootify.spring_car_rental.repos.AppUserRepository;
import java.util.Collections;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class JwtUserDetailsService implements UserDetailsService {

    public static final String USER = "USER";
    public static final String ROLE_USER = "ROLE_" + USER;

    private final AppUserRepository appUserRepository;

    public JwtUserDetailsService(final AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) {
        final AppUser appUser = appUserRepository.findByEmailIgnoreCase(username);
        if (appUser == null) {
            log.warn("user not found: {}", username);
            throw new UsernameNotFoundException("User " + username + " not found");
        }
        return new User(username, appUser.getPassword(), Collections.singletonList(new SimpleGrantedAuthority(appUser.getRole())));
    }

}
