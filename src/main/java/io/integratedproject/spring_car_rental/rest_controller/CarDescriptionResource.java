package io.integratedproject.spring_car_rental.rest_controller;

import io.integratedproject.spring_car_rental.DTO.CarDescriptionDTO;
import io.integratedproject.spring_car_rental.service.interf.CarDescriptionService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/carDescriptions", produces = MediaType.APPLICATION_JSON_VALUE)
@SecurityRequirement(name = "bearer-jwt")
public class CarDescriptionResource {

    private final CarDescriptionService carDescriptionService;

    public CarDescriptionResource(final CarDescriptionService carDescriptionService) {
        this.carDescriptionService = carDescriptionService;
    }

    @GetMapping
    public ResponseEntity<List<CarDescriptionDTO>> getAllCarDescriptions(
            @RequestParam(required = false) final String filter) {
        return ResponseEntity.ok(carDescriptionService.findAll(filter));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarDescriptionDTO> getCarDescription(@PathVariable final Long id) {
        return ResponseEntity.ok(carDescriptionService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createCarDescription(
            @RequestBody @Valid final CarDescriptionDTO carDescriptionDTO) {
        return new ResponseEntity<>(carDescriptionService.create(carDescriptionDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCarDescription(@PathVariable final Long id,
            @RequestBody @Valid final CarDescriptionDTO carDescriptionDTO) {
        carDescriptionService.update(id, carDescriptionDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteCarDescription(@PathVariable final Long id) {
        carDescriptionService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
