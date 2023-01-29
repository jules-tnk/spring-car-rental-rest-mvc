package io.bootify.spring_car_rental.rest_controller.client_controller;

import io.bootify.spring_car_rental.DTO.incoming_request.CarRentalRequest;
import io.bootify.spring_car_rental.domain.CarRental;
import io.bootify.spring_car_rental.domain.RentalStatus;
import io.bootify.spring_car_rental.repos.AppUserRepository;
import io.bootify.spring_car_rental.repos.CarRentalRepository;
import io.bootify.spring_car_rental.repos.CarRepository;
import io.bootify.spring_car_rental.repos.ClientRepository;
import io.bootify.spring_car_rental.service.impl.CarRentalServiceImpl;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping(value = "/api-client/", produces = MediaType.APPLICATION_JSON_VALUE)
@SecurityRequirement(name = "bearer-jwt")
public class ClientRoleController {
    private final CarRentalServiceImpl carRentalService;
    private final CarRentalRepository carRentalRepository;
    private final ClientRepository clientRepository;
    private final CarRepository carRepository;
    private AppUserRepository appUserRepository;

    public ClientRoleController(CarRentalServiceImpl carRentalService,
                                CarRentalRepository carRentalRepository,
                                ClientRepository clientRepository,
                                CarRepository carRepository,
                                AppUserRepository appUserRepository) {
        this.carRentalService = carRentalService;
        this.carRentalRepository = carRentalRepository;
        this.clientRepository = clientRepository;
        this.carRepository = carRepository;
        this.appUserRepository = appUserRepository;
    }


    @PostMapping(value = "/rental")
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Void> createCarRental(
            @RequestBody @Valid final CarRentalRequest carRentalRequest) {

        // Retrieve username (email) from security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if ((authentication instanceof AnonymousAuthenticationToken)) {
            return null;
        }

        String currentUserEmail = authentication.getName();

        System.out.println("Current user "+currentUserEmail);

        CarRental newCarRental = new CarRental();

        // find and set client by user email
        newCarRental.setTenant(
                clientRepository.findByEmailIgnoreCase(currentUserEmail)
        );

        // find and set first car available by car description id
        newCarRental.setCar(
                carRepository.findFirstByCarDescription_IdAndAndIsAvailableIsTrue(
                        carRentalRequest.getCarDescriptionId()
                )
        );

        // set start and end date from dates in request
        //DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        newCarRental.setStartDate(
                LocalDate.parse(carRentalRequest.getStartDateString(), dateFormatter)
        );

        newCarRental.setEndDate(
                LocalDate.parse(carRentalRequest.getEndDateString(), dateFormatter)
        );


        newCarRental.setIsWithDriver(
                carRentalRequest.getIsWithDriver()
        );
        newCarRental.setStatus(RentalStatus.RESERVED);

        // set car new availability

        carRentalRepository.save(newCarRental);

        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }
}
