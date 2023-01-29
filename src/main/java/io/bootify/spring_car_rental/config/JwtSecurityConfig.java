package io.bootify.spring_car_rental.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.bootify.spring_car_rental.DTO.response.ErrorResponse;
import io.bootify.spring_car_rental.domain.user_management.UserRole;
import io.bootify.spring_car_rental.service.security.JwtUserDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
public class JwtSecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;
    private final ObjectMapper objectMapper;

    public JwtSecurityConfig(final JwtRequestFilter jwtRequestFilter,
            final ObjectMapper objectMapper) {
        this.jwtRequestFilter = jwtRequestFilter;
        this.objectMapper = objectMapper;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            final AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain configure(final HttpSecurity http) throws Exception {
        return http.cors().and()
                .csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/api/**").hasAnyAuthority(UserRole.AGENT, UserRole.MANAGER, UserRole.DRIVER)
                .requestMatchers("/api-client/**").hasAuthority(UserRole.CLIENT)
                    .anyRequest().permitAll().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .exceptionHandling().authenticationEntryPoint((request, response, ex) -> {
                    final ErrorResponse errorResponse = new ErrorResponse();
                    errorResponse.setException("Unauthorized");
                    errorResponse.setHttpStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getOutputStream().println(objectMapper.writeValueAsString(errorResponse));
                }).and()
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}
