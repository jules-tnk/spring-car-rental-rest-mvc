package io.integratedproject.spring_car_rental.rest_controller.client_controller;

import io.integratedproject.spring_car_rental.DTO.incoming_request.CarRentalRequest;
import io.integratedproject.spring_car_rental.DTO.response.CarRentalResponseDTO;
import io.integratedproject.spring_car_rental.DTO.response.ProfileResponse;
import io.integratedproject.spring_car_rental.entity.Car;
import io.integratedproject.spring_car_rental.entity.CarRental;
import io.integratedproject.spring_car_rental.entity.RentalStatus;
import io.integratedproject.spring_car_rental.entity.user_management.AppUser;
import io.integratedproject.spring_car_rental.entity.user_management.Driver;
import io.integratedproject.spring_car_rental.repository.*;
import io.integratedproject.spring_car_rental.service.impl.CarRentalServiceImpl;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api-client/", produces = MediaType.APPLICATION_JSON_VALUE)
@SecurityRequirement(name = "bearer-jwt")
public class ClientRoleController {
    private final CarRentalServiceImpl carRentalService;
    private final CarRentalRepository carRentalRepository;
    private final ClientRepository clientRepository;
    private final CarRepository carRepository;
    private AppUserRepository appUserRepository;
    private final DriverRepository driverRepository;

    public ClientRoleController(CarRentalServiceImpl carRentalService,
                                CarRentalRepository carRentalRepository,
                                ClientRepository clientRepository,
                                CarRepository carRepository,
                                AppUserRepository appUserRepository,
                                DriverRepository driverRepository) {
        this.carRentalService = carRentalService;
        this.carRentalRepository = carRentalRepository;
        this.clientRepository = clientRepository;
        this.carRepository = carRepository;
        this.appUserRepository = appUserRepository;
        this.driverRepository = driverRepository;
    }


    @PostMapping(value = "/rental")
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Void> createCarRental(
            @RequestBody @Valid final CarRentalRequest carRentalRequest) throws NotFoundException {

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
                appUserRepository.findByEmailIgnoreCase(currentUserEmail)
        );

        // find and set first car available by car description id
        Optional<Car> carToRentOpt = carRepository.findFirstByCarDescription_IdAndAndIsAvailableIsTrue(
                carRentalRequest.getCarDescriptionId()
        );
        try {

            if (carToRentOpt.isEmpty()){
                throw new NotFoundException("No car available");
            }
        }
        catch (Exception e) {
            throw new NotFoundException("No car available");
        }

        newCarRental.setCar(carToRentOpt.get());
        carToRentOpt.get().setIsAvailable(false);
        carRepository.save(carToRentOpt.get());

        // set start and end date from dates in request
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        newCarRental.setStartDate(
                LocalDate.parse(carRentalRequest.getStartDateString(), dateFormatter)
        );

        newCarRental.setEndDate(
                LocalDate.parse(carRentalRequest.getEndDateString(), dateFormatter)
        );
        // calculate total price
        newCarRental.setTotalPrice(
                carRentalService.calculateTotalPrice(
                        newCarRental.getStartDate(),
                        newCarRental.getEndDate(),
                        newCarRental.getCar().getCarDescription().getPricePerDay()
                )
        );

        if (carRentalRequest.getIsWithDriver()){
            newCarRental.setIsWithDriver(true);

            Optional<Driver> driverOpt = driverRepository.findFirstByIsAvailableIsTrue();
            if (driverOpt.isEmpty()){
                throw new NotFoundException("No driver available");
            }
            driverOpt.get().setIsAvailable(false);
            driverRepository.save(driverOpt.get());
            newCarRental.setDriver(driverOpt.get());
            newCarRental.setTotalPrice(newCarRental.getTotalPrice()+120);
        }


        newCarRental.setStatus(RentalStatus.RESERVED);

        // save the new car rental
        carRentalRepository.save(newCarRental);

        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }

    @GetMapping(value = "/profile")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<ProfileResponse> getProfileInfo() {

        // Retrieve username (email) from security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if ((authentication instanceof AnonymousAuthenticationToken)) {
            return null;
        }

        String currentUserEmail = authentication.getName();
        System.out.println("Current user "+currentUserEmail);
        AppUser currentUser = appUserRepository.findByEmailIgnoreCase(currentUserEmail);

        ProfileResponse profileResponse = new ProfileResponse();
        profileResponse.setCarRentalHistory(
                carRentalService.findByTenantEmail(currentUserEmail)
        );

        if (currentUser.getRole().equals("DRIVER")) {
            profileResponse.setMissionHistory(
                    carRentalService.findByDriverEmail(currentUserEmail)
            );
        }

        return new ResponseEntity<>(profileResponse, HttpStatus.CREATED);
    }

    @GetMapping("/history/{id}")
    public ResponseEntity<CarRentalResponseDTO> getCarRental(@PathVariable("id") final Long id) {
        return ResponseEntity.ok(carRentalService.findByIdForRestResponse(id));
    }

    @GetMapping("cancel/{id}")
    public ResponseEntity<Void> cancelCarRental(@PathVariable final Long id) {
        carRentalService.cancelCarRental(id);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
