package io.bootify.spring_car_rental.service.security;

import io.bootify.spring_car_rental.DTO.incoming_request.RegistrationRequest;
import io.bootify.spring_car_rental.domain.user_management.Agent;
import io.bootify.spring_car_rental.domain.user_management.Client;
import io.bootify.spring_car_rental.domain.user_management.Driver;
import io.bootify.spring_car_rental.domain.user_management.Manager;
import io.bootify.spring_car_rental.repos.AgentRepository;
import io.bootify.spring_car_rental.repos.ClientRepository;
import io.bootify.spring_car_rental.repos.DriverRepository;
import io.bootify.spring_car_rental.repos.ManagerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class RegistrationService {
    private final ManagerRepository managerRepository;
    private final AgentRepository agentRepository;
    private final DriverRepository driverRepository;
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationService(final ClientRepository appUserRepository,
            final PasswordEncoder passwordEncoder,
                               DriverRepository driverRepository,
                               AgentRepository agentRepository,
                               ManagerRepository managerRepository) {
        this.clientRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.driverRepository = driverRepository;
        this.agentRepository = agentRepository;
        this.managerRepository = managerRepository;
    }

    public boolean emailExists(final RegistrationRequest registrationRequest) {
        return clientRepository.existsByEmailIgnoreCase(registrationRequest.getEmail());
    }

    public void registerClient(final RegistrationRequest registrationRequest) {
        log.info("registering new client: {}", registrationRequest.getEmail());

        Client appUser = new Client();
        appUser.setFirstName(registrationRequest.getFirstName());
        appUser.setLastName(registrationRequest.getLastName());
        appUser.setEmail(registrationRequest.getEmail());
        appUser.setPhoneNumber(registrationRequest.getPhoneNumber());
        appUser.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        clientRepository.save(appUser);
    }


}
