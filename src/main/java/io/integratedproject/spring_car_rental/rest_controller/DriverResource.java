package io.integratedproject.spring_car_rental.rest_controller;

import io.integratedproject.spring_car_rental.DTO.user_management.DriverDTO;
import io.integratedproject.spring_car_rental.service.interf.DriverService;
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
@RequestMapping(value = "/api/drivers", produces = MediaType.APPLICATION_JSON_VALUE)
@SecurityRequirement(name = "bearer-jwt")
public class DriverResource {

    private final DriverService driverService;

    public DriverResource(final DriverService driverService) {
        this.driverService = driverService;
    }

    @GetMapping
    public ResponseEntity<List<DriverDTO>> getAllDrivers(
            @RequestParam(required = false) final String filter) {
        return ResponseEntity.ok(driverService.findAll(filter));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DriverDTO> getDriver(@PathVariable final Long id) {
        return ResponseEntity.ok(driverService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createDriver(@RequestBody @Valid final DriverDTO driverDTO) {
        return new ResponseEntity<>(driverService.create(driverDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateDriver(@PathVariable final Long id,
            @RequestBody @Valid final DriverDTO driverDTO) {
        driverService.update(id, driverDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteDriver(@PathVariable final Long id) {
        driverService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
