package io.integratedproject.spring_car_rental.rest_controller.client_controller;

import io.integratedproject.spring_car_rental.DTO.PaymentDTO;
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
import io.integratedproject.spring_car_rental.service.impl.CarServiceImpl;
import io.integratedproject.spring_car_rental.service.impl.DriverServiceImpl;
import io.integratedproject.spring_car_rental.service.impl.PaymentServiceImpl;
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
    private final PaymentServiceImpl paymentService;
    private final ClientRepository clientRepository;
    private final CarRepository carRepository;
    private AppUserRepository appUserRepository;
    private final DriverServiceImpl driverService;
    private final DriverRepository driverRepository;
    private final CarServiceImpl carService;
    private final CarDescriptionRepository carDescriptionRepository;

    public ClientRoleController(CarRentalServiceImpl carRentalService,
                                CarRentalRepository carRentalRepository,
                                PaymentServiceImpl paymentService, ClientRepository clientRepository,
                                CarRepository carRepository,
                                AppUserRepository appUserRepository,
                                DriverServiceImpl driverService,
                                DriverRepository driverRepository,
                                CarServiceImpl carService,
                                CarDescriptionRepository carDescriptionRepository) {
        this.carRentalService = carRentalService;
        this.carRentalRepository = carRentalRepository;
        this.paymentService = paymentService;
        this.clientRepository = clientRepository;
        this.carRepository = carRepository;
        this.appUserRepository = appUserRepository;
        this.driverService = driverService;
        this.driverRepository = driverRepository;
        this.carService = carService;
        this.carDescriptionRepository = carDescriptionRepository;
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

        CarRental newCarRental = new CarRental();

        // find and set client by user email
        newCarRental.setTenant(
                appUserRepository.findByEmailIgnoreCase(currentUserEmail)
        );

        // set start and end date from dates in request
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(carRentalRequest.getStartDateString(), dateFormatter);
        LocalDate endDate = LocalDate.parse(carRentalRequest.getEndDateString(), dateFormatter);
        newCarRental.setStartDate(startDate);
        newCarRental.setEndDate(endDate);

        // find and set first car available by car description
        Car carToRent = carService.selectAvailableCar(
                carDescriptionRepository.findById(carRentalRequest.getCarDescriptionId()).get(),
                startDate,
                endDate
        );
        if (carToRent == null){
            throw new NotFoundException("No car available");
        }
        newCarRental.setCar(carToRent);

        // calculate total price
        newCarRental.setTotalPrice(
                carRentalService.calculateTotalPrice(
                        newCarRental.getStartDate(),
                        newCarRental.getEndDate(),
                        newCarRental.getCar().getCarDescription().getPricePerDay()
                )
        );

        // select and add driver if wished by the tenant
        if (carRentalRequest.getIsWithDriver()){
            Driver driver = driverService.selectAvailableDriver(startDate, endDate);

            if (driver == null){
                throw new NotFoundException("No driver Available");
            }

            newCarRental.setIsWithDriver(true);
            newCarRental.setDriver(driver);
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

    @PostMapping("/add/payment")
    public ResponseEntity<Void> addPayment(@RequestBody final PaymentDTO paymentDTO) {
        paymentService.create(paymentDTO);
        CarRental carRental = carRentalRepository.findById(paymentDTO.getCarRentalId()).get();
        carRental.setAlreadyPaid(carRental.getAlreadyPaid()+paymentDTO.getAmount());
        if (carRental.getAlreadyPaid() >= carRental.getTotalPrice()){
            carRental.setIsPaymentCompleted(true);
        }
        carRentalRepository.save(carRental);
        return ResponseEntity.ok().build();
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
