package io.bootify.spring_car_rental.datainitializer;

import io.bootify.spring_car_rental.DTO.incoming_request.RegistrationRequest;
import io.bootify.spring_car_rental.rest_controller.public_controller.AuthenticationResource;
import io.bootify.spring_car_rental.rest_controller.public_controller.RegistrationResource;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UserInitializer implements ApplicationListener<ContextRefreshedEvent> {

    private final RegistrationResource registrationResource;
    private AuthenticationResource authenticationResource;

    public UserInitializer(RegistrationResource registrationResource, AuthenticationResource authenticationResource) {
        this.registrationResource = registrationResource;
        this.authenticationResource = authenticationResource;
    }

    private boolean alreadySetup = false;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup){return;}

        RegistrationRequest newUserRequest = new RegistrationRequest(
                "Juju",
                "TNK",
                "juju@gmail.com",
                608917921,
                "sacarina"

        );

        registrationResource.register(newUserRequest);
        //ClientAuthenticationResponse login = authenticationResource.authenticate(
        //        new AuthenticationRequest(
        //                newUserRequest.getEmail(),
        //                newUserRequest.getPassword()
        //        )
        //);

        //System.out.println(login);

        alreadySetup = true;
    }
}
