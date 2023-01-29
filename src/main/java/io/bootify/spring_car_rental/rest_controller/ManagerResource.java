package io.bootify.spring_car_rental.rest_controller;

import io.bootify.spring_car_rental.DTO.user_management.ManagerDTO;
import io.bootify.spring_car_rental.service.interf.ManagerService;
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
@RequestMapping(value = "/api/managers", produces = MediaType.APPLICATION_JSON_VALUE)
@SecurityRequirement(name = "bearer-jwt")
public class ManagerResource {

    private final ManagerService managerService;

    public ManagerResource(final ManagerService managerService) {
        this.managerService = managerService;
    }

    @GetMapping
    public ResponseEntity<List<ManagerDTO>> getAllManagers(
            @RequestParam(required = false) final String filter) {
        return ResponseEntity.ok(managerService.findAll(filter));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ManagerDTO> getManager(@PathVariable final Long id) {
        return ResponseEntity.ok(managerService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createManager(@RequestBody @Valid final ManagerDTO managerDTO) {
        return new ResponseEntity<>(managerService.create(managerDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateManager(@PathVariable final Long id,
            @RequestBody @Valid final ManagerDTO managerDTO) {
        managerService.update(id, managerDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteManager(@PathVariable final Long id) {
        managerService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
