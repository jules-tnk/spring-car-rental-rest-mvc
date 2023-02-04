package io.bootify.spring_car_rental.rest_controller.public_controller;

import io.bootify.spring_car_rental.DTO.incoming_request.AuthenticationRequest;
import io.bootify.spring_car_rental.DTO.response.AuthenticationResponse;
import io.bootify.spring_car_rental.repos.ClientRepository;
import io.bootify.spring_car_rental.service.interf.AppUserService;
import io.bootify.spring_car_rental.service.interf.ClientService;
import io.bootify.spring_car_rental.service.security.JwtTokenService;
import io.bootify.spring_car_rental.service.security.JwtUserDetailsService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


@RestController
public class AuthenticationResource {

    private final AuthenticationManager authenticationManager;
    private final JwtUserDetailsService jwtUserDetailsService;
    private final JwtTokenService jwtTokenService;

    private final AppUserService appUserService;

    public AuthenticationResource(final AuthenticationManager authenticationManager,
                                  final JwtUserDetailsService jwtUserDetailsService,
                                  final JwtTokenService jwtTokenService,
                                  AppUserService appUserService) {
        this.authenticationManager = authenticationManager;
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.jwtTokenService = jwtTokenService;
        this.appUserService = appUserService;
    }

    @PostMapping("/login")
    public AuthenticationResponse authenticate(
            @RequestBody @Valid final AuthenticationRequest authenticationRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getEmail(), authenticationRequest.getPassword()));
        } catch (final BadCredentialsException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(authenticationRequest.getEmail());
        final AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setAccessToken(jwtTokenService.generateToken(userDetails));
        authenticationResponse.setUser(
                appUserService.getByEmail(authenticationRequest.getEmail())
        );
        return authenticationResponse;
    }

}
