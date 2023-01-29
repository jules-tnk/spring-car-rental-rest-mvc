package io.bootify.spring_car_rental.service.impl;

import io.bootify.spring_car_rental.domain.Agency;
import io.bootify.spring_car_rental.domain.user_management.Manager;
import io.bootify.spring_car_rental.DTO.AgencyDTO;
import io.bootify.spring_car_rental.repos.AgencyRepository;
import io.bootify.spring_car_rental.repos.ManagerRepository;
import io.bootify.spring_car_rental.service.interf.AgencyService;
import io.bootify.spring_car_rental.util.NotFoundException;
import io.bootify.spring_car_rental.util.WebUtils;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class AgencyServiceImpl implements AgencyService {

    private final AgencyRepository agencyRepository;
    private final ManagerRepository managerRepository;

    public AgencyServiceImpl(final AgencyRepository agencyRepository,
            final ManagerRepository managerRepository) {
        this.agencyRepository = agencyRepository;
        this.managerRepository = managerRepository;
    }

    @Override
    public List<AgencyDTO> findAll() {
        final List<Agency> agencys = agencyRepository.findAll(Sort.by("id"));
        return agencys.stream()
                .map((agency) -> mapToDTO(agency, new AgencyDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public AgencyDTO get(final Long id) {
        return agencyRepository.findById(id)
                .map(agency -> mapToDTO(agency, new AgencyDTO()))
                .orElseThrow(() -> new NotFoundException());
    }

    @Override
    public Long create(final AgencyDTO agencyDTO) {
        final Agency agency = new Agency();
        mapToEntity(agencyDTO, agency);
        return agencyRepository.save(agency).getId();
    }

    @Override
    public void update(final Long id, final AgencyDTO agencyDTO) {
        final Agency agency = agencyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException());
        mapToEntity(agencyDTO, agency);
        agencyRepository.save(agency);
    }

    @Override
    public void delete(final Long id) {
        agencyRepository.deleteById(id);
    }

    private AgencyDTO mapToDTO(final Agency agency, final AgencyDTO agencyDTO) {
        agencyDTO.setId(agency.getId());
        agencyDTO.setName(agency.getName());
        agencyDTO.setAddress(agency.getAddress());
        agencyDTO.setCity(agency.getCity());
        agencyDTO.setManager(agency.getManager() == null ? null : agency.getManager().getId());
        return agencyDTO;
    }

    private Agency mapToEntity(final AgencyDTO agencyDTO, final Agency agency) {
        agency.setName(agencyDTO.getName());
        agency.setAddress(agencyDTO.getAddress());
        agency.setCity(agencyDTO.getCity());
        final Manager manager = agencyDTO.getManager() == null ? null : managerRepository.findById(agencyDTO.getManager())
                .orElseThrow(() -> new NotFoundException("manager not found"));
        agency.setManager(manager);
        return agency;
    }

    @Transactional
    @Override
    public String getReferencedWarning(final Long id) {
        final Agency agency = agencyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException());
        if (!agency.getAgencyAgents().isEmpty()) {
            return WebUtils.getMessage("agency.agent.oneToMany.referenced", agency.getAgencyAgents().iterator().next().getId());
        }
        return null;
    }

}
