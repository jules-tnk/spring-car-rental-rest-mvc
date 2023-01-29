package io.bootify.spring_car_rental.service.security;

import io.bootify.spring_car_rental.DTO.incoming_request.RegistrationRequest;
import io.bootify.spring_car_rental.domain.user_management.Client;
import io.bootify.spring_car_rental.repos.ClientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class RegistrationService {

    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationService(final ClientRepository appUserRepository,
            final PasswordEncoder passwordEncoder) {
        this.clientRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean emailExists(final RegistrationRequest registrationRequest) {
        return clientRepository.existsByEmailIgnoreCase(registrationRequest.getEmail());
    }

    public void registerClient(final RegistrationRequest registrationRequest) {
        log.info("registering new user: {}", registrationRequest.getEmail());

        Client appUser = new Client();
        appUser.setFirstName(registrationRequest.getFirstName());
        appUser.setLastName(registrationRequest.getLastName());
        appUser.setEmail(registrationRequest.getEmail());
        appUser.setPhoneNumber(registrationRequest.getPhoneNumber());
        appUser.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        clientRepository.save(appUser);
    }

}
