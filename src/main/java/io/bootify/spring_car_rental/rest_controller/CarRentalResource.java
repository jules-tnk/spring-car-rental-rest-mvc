package io.bootify.spring_car_rental.rest_controller;

import io.bootify.spring_car_rental.DTO.CarRentalDTO;
import io.bootify.spring_car_rental.service.interf.CarRentalService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/carRentals", produces = MediaType.APPLICATION_JSON_VALUE)
@SecurityRequirement(name = "bearer-jwt")
public class CarRentalResource {

    private final CarRentalService carRentalService;

    public CarRentalResource(final CarRentalService carRentalService) {
        this.carRentalService = carRentalService;
    }

    @GetMapping
    public ResponseEntity<List<CarRentalDTO>> getAllCarRentals() {
        return ResponseEntity.ok(carRentalService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarRentalDTO> getCarRental(@PathVariable final Long id) {
        return ResponseEntity.ok(carRentalService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createCarRental(
            @RequestBody @Valid final CarRentalDTO carRentalDTO) {
        return new ResponseEntity<>(carRentalService.create(carRentalDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCarRental(@PathVariable final Long id,
            @RequestBody @Valid final CarRentalDTO carRentalDTO) {
        carRentalService.update(id, carRentalDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteCarRental(@PathVariable final Long id) {
        carRentalService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
