package io.integratedproject.spring_car_rental.service.impl;

import io.integratedproject.spring_car_rental.entity.user_management.Manager;
import io.integratedproject.spring_car_rental.DTO.user_management.ManagerDTO;
import io.integratedproject.spring_car_rental.repository.ManagerRepository;
import io.integratedproject.spring_car_rental.service.interf.ManagerService;
import io.integratedproject.spring_car_rental.util.NotFoundException;
import io.integratedproject.spring_car_rental.util.WebUtils;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class ManagerServiceImpl implements ManagerService {

    private final ManagerRepository managerRepository;
    private final PasswordEncoder passwordEncoder;

    public ManagerServiceImpl(final ManagerRepository managerRepository, PasswordEncoder passwordEncoder) {
        this.managerRepository = managerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<ManagerDTO> findAll(final String filter) {
        List<Manager> managers;
        final Sort sort = Sort.by("id");
        if (filter != null) {
            Long longFilter = null;
            try {
                longFilter = Long.parseLong(filter);
            } catch (final NumberFormatException numberFormatException) {
                // keep null - no parseable input
            }
            managers = managerRepository.findAllById(longFilter, sort);
        } else {
            managers = managerRepository.findAll(sort);
        }
        return managers.stream()
                .map((manager) -> mapToDTO(manager, new ManagerDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public ManagerDTO get(final Long id) {
        return managerRepository.findById(id)
                .map(manager -> mapToDTO(manager, new ManagerDTO()))
                .orElseThrow(() -> new NotFoundException());
    }

    @Override
    public Long create(final ManagerDTO managerDTO) {
        final Manager manager = new Manager();
        mapToEntity(managerDTO, manager);
        manager.setPassword(passwordEncoder.encode(managerDTO.getPassword()));
        return managerRepository.save(manager).getId();
    }

    @Override
    public void update(final Long id, final ManagerDTO managerDTO) {
        final Manager manager = managerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException());
        mapToEntity(managerDTO, manager);
        managerRepository.save(manager);
    }

    @Override
    public void delete(final Long id) {
        managerRepository.deleteById(id);
    }

    private ManagerDTO mapToDTO(final Manager manager, final ManagerDTO managerDTO) {
        managerDTO.setId(manager.getId());
        managerDTO.setFirstName(manager.getFirstName());
        managerDTO.setLastName(manager.getLastName());
        managerDTO.setEmail(manager.getEmail());
        managerDTO.setPhoneNumber(manager.getPhoneNumber());
        return managerDTO;
    }

    private Manager mapToEntity(final ManagerDTO managerDTO, final Manager manager) {
        manager.setFirstName(managerDTO.getFirstName());
        manager.setLastName(managerDTO.getLastName());
        manager.setEmail(managerDTO.getEmail());
        manager.setPhoneNumber(managerDTO.getPhoneNumber());
        return manager;
    }

    @Transactional
    @Override
    public String getReferencedWarning(final Long id) {
        final Manager manager = managerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException());
        if (manager.getAgency() != null) {
            return WebUtils.getMessage("manager.agency.oneToOne.referenced", manager.getAgency().getId());
        }
        return null;
    }

}
