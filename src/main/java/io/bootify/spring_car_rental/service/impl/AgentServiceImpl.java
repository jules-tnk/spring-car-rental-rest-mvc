package io.bootify.spring_car_rental.service.impl;

import io.bootify.spring_car_rental.domain.Agency;
import io.bootify.spring_car_rental.domain.user_management.Agent;
import io.bootify.spring_car_rental.DTO.user_management.AgentDTO;
import io.bootify.spring_car_rental.repos.AgencyRepository;
import io.bootify.spring_car_rental.repos.AgentRepository;
import io.bootify.spring_car_rental.service.interf.AgentService;
import io.bootify.spring_car_rental.util.NotFoundException;
import io.bootify.spring_car_rental.util.WebUtils;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AgentServiceImpl implements AgentService {

    private final AgentRepository agentRepository;
    private final AgencyRepository agencyRepository;
    private final PasswordEncoder passwordEncoder;

    public AgentServiceImpl(final AgentRepository agentRepository,
                            final AgencyRepository agencyRepository, PasswordEncoder passwordEncoder) {
        this.agentRepository = agentRepository;
        this.agencyRepository = agencyRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public List<AgentDTO> findAll(final String filter) {
        List<Agent> agents;
        final Sort sort = Sort.by("id");
        if (filter != null) {
            Long longFilter = null;
            try {
                longFilter = Long.parseLong(filter);
            } catch (final NumberFormatException numberFormatException) {
                // keep null - no parseable input
            }
            agents = agentRepository.findAllById(longFilter, sort);
        } else {
            agents = agentRepository.findAll(sort);
        }
        return agents.stream()
                .map((agent) -> mapToDTO(agent, new AgentDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public AgentDTO get(final Long id) {
        return agentRepository.findById(id)
                .map(agent -> mapToDTO(agent, new AgentDTO()))
                .orElseThrow(() -> new NotFoundException());
    }

    @Override
    public Long create(final AgentDTO agentDTO) {
        final Agent agent = new Agent();
        mapToEntity(agentDTO, agent);
        agent.setPassword(passwordEncoder.encode(agentDTO.getPassword()));
        return agentRepository.save(agent).getId();
    }

    @Override
    public void update(final Long id, final AgentDTO agentDTO) {
        final Agent agent = agentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException());
        mapToEntity(agentDTO, agent);
        agentRepository.save(agent);
    }

    @Override
    public void delete(final Long id) {
        agentRepository.deleteById(id);
    }

    private AgentDTO mapToDTO(final Agent agent, final AgentDTO agentDTO) {
        agentDTO.setId(agent.getId());
        agentDTO.setFirstName(agent.getFirstName());
        agentDTO.setLastName(agent.getLastName());
        agentDTO.setEmail(agent.getEmail());
        agentDTO.setPhoneNumber(agent.getPhoneNumber());
        agentDTO.setAgency(agent.getAgency() == null ? null : agent.getAgency().getId());
        return agentDTO;
    }

    private Agent mapToEntity(final AgentDTO agentDTO, final Agent agent) {
        agent.setFirstName(agentDTO.getFirstName());
        agent.setLastName(agentDTO.getLastName());
        agent.setEmail(agentDTO.getEmail());
        agent.setPhoneNumber(agentDTO.getPhoneNumber());
        final Agency agency = agentDTO.getAgency() == null ? null : agencyRepository.findById(agentDTO.getAgency())
                .orElseThrow(() -> new NotFoundException("agency not found"));
        agent.setAgency(agency);
        return agent;
    }

    @Transactional
    @Override
    public String getReferencedWarning(final Long id) {
        final Agent agent = agentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException());
        if (!agent.getAgentForConfirmationCarRentals().isEmpty()) {
            return WebUtils.getMessage("agent.carRental.manyToOne.referenced", agent.getAgentForConfirmationCarRentals().iterator().next().getId());
        } else if (!agent.getAgentForReturnCarRentals().isEmpty()) {
            return WebUtils.getMessage("agent.carRental.manyToOne.referenced", agent.getAgentForReturnCarRentals().iterator().next().getId());
        }
        return null;
    }

}
