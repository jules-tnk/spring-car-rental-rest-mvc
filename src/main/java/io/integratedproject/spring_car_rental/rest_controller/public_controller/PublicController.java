package io.integratedproject.spring_car_rental.rest_controller.public_controller;

import io.integratedproject.spring_car_rental.DTO.CarDescriptionDTO;
import io.integratedproject.spring_car_rental.service.impl.CarDescriptionServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api-public/", produces = MediaType.APPLICATION_JSON_VALUE)
@SecurityRequirement(name = "bearer-jwt")
public class PublicController {

    private final CarDescriptionServiceImpl carDescriptionService;

    public PublicController(CarDescriptionServiceImpl carDescriptionService) {
        this.carDescriptionService = carDescriptionService;
    }

    @GetMapping(value = "/car-description")
    public ResponseEntity<List<CarDescriptionDTO>> getAllCarDescriptions(
            @RequestParam(required = false) final String filter) {
        return ResponseEntity.ok(carDescriptionService.findAll(filter));
    }

    @GetMapping("/car-description/{id}")
    public ResponseEntity<CarDescriptionDTO> getCarDescription(@PathVariable final Long id) {
        return ResponseEntity.ok(carDescriptionService.get(id));
    }
}
