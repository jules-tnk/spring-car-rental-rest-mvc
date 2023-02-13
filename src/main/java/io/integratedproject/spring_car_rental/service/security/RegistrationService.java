package io.integratedproject.spring_car_rental.service.security;

import io.integratedproject.spring_car_rental.DTO.incoming_request.RegistrationRequest;
import io.integratedproject.spring_car_rental.entity.user_management.Client;
import io.integratedproject.spring_car_rental.repository.AgentRepository;
import io.integratedproject.spring_car_rental.repository.ClientRepository;
import io.integratedproject.spring_car_rental.repository.DriverRepository;
import io.integratedproject.spring_car_rental.repository.ManagerRepository;
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
