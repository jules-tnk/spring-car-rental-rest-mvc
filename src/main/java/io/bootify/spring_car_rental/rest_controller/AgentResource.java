package io.bootify.spring_car_rental.rest_controller;

import io.bootify.spring_car_rental.DTO.user_management.AgentDTO;
import io.bootify.spring_car_rental.service.interf.AgentService;
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
@RequestMapping(value = "/api/agents", produces = MediaType.APPLICATION_JSON_VALUE)
@SecurityRequirement(name = "bearer-jwt")
public class AgentResource {

    private final AgentService agentService;

    public AgentResource(final AgentService agentService) {
        this.agentService = agentService;
    }

    @GetMapping
    public ResponseEntity<List<AgentDTO>> getAllAgents(
            @RequestParam(required = false) final String filter) {
        return ResponseEntity.ok(agentService.findAll(filter));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgentDTO> getAgent(@PathVariable final Long id) {
        return ResponseEntity.ok(agentService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createAgent(@RequestBody @Valid final AgentDTO agentDTO) {
        return new ResponseEntity<>(agentService.create(agentDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateAgent(@PathVariable final Long id,
            @RequestBody @Valid final AgentDTO agentDTO) {
        agentService.update(id, agentDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteAgent(@PathVariable final Long id) {
        agentService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
