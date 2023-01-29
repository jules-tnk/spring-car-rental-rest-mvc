package io.bootify.spring_car_rental.rest_controller.public_controller;

import io.bootify.spring_car_rental.DTO.incoming_request.RegistrationRequest;
import io.bootify.spring_car_rental.service.security.RegistrationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


@RestController
public class RegistrationResource {

    private final RegistrationService registrationService;

    public RegistrationResource(final RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(
            @RequestBody @Valid final RegistrationRequest registrationRequest) {
        if (registrationService.emailExists(registrationRequest)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "registration.register.taken");
        }
        registrationService.registerClient(registrationRequest);
        return ResponseEntity.ok().build();
    }

}
