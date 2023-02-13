package io.integratedproject.spring_car_rental.rest_controller;

import io.integratedproject.spring_car_rental.DTO.AgencyDTO;
import io.integratedproject.spring_car_rental.service.interf.AgencyService;
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
@RequestMapping(value = "/api/agencys", produces = MediaType.APPLICATION_JSON_VALUE)
@SecurityRequirement(name = "bearer-jwt")
public class AgencyResource {

    private final AgencyService agencyService;

    public AgencyResource(final AgencyService agencyService) {
        this.agencyService = agencyService;
    }

    @GetMapping
    public ResponseEntity<List<AgencyDTO>> getAllAgencys() {
        return ResponseEntity.ok(agencyService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgencyDTO> getAgency(@PathVariable final Long id) {
        return ResponseEntity.ok(agencyService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createAgency(@RequestBody @Valid final AgencyDTO agencyDTO) {
        return new ResponseEntity<>(agencyService.create(agencyDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateAgency(@PathVariable final Long id,
            @RequestBody @Valid final AgencyDTO agencyDTO) {
        agencyService.update(id, agencyDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteAgency(@PathVariable final Long id) {
        agencyService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
