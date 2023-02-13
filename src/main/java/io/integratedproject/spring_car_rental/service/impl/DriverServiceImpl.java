package io.integratedproject.spring_car_rental.service.impl;

import io.integratedproject.spring_car_rental.entity.user_management.Driver;
import io.integratedproject.spring_car_rental.DTO.user_management.DriverDTO;
import io.integratedproject.spring_car_rental.repository.DriverRepository;
import io.integratedproject.spring_car_rental.service.interf.DriverService;
import io.integratedproject.spring_car_rental.util.NotFoundException;
import io.integratedproject.spring_car_rental.util.WebUtils;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;
    private final PasswordEncoder passwordEncoder;

    public DriverServiceImpl(final DriverRepository driverRepository, PasswordEncoder passwordEncoder) {
        this.driverRepository = driverRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<DriverDTO> findAll(final String filter) {
        List<Driver> drivers;
        final Sort sort = Sort.by("id");
        if (filter != null) {
            Long longFilter = null;
            try {
                longFilter = Long.parseLong(filter);
            } catch (final NumberFormatException numberFormatException) {
                // keep null - no parseable input
            }
            drivers = driverRepository.findAllById(longFilter, sort);
        } else {
            drivers = driverRepository.findAll(sort);
        }
        return drivers.stream()
                .map((driver) -> mapToDTO(driver, new DriverDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public DriverDTO get(final Long id) {
        return driverRepository.findById(id)
                .map(driver -> mapToDTO(driver, new DriverDTO()))
                .orElseThrow(() -> new NotFoundException());
    }

    @Override
    public Long create(final DriverDTO driverDTO) {
        final Driver driver = new Driver();
        mapToEntity(driverDTO, driver);
        driver.setPassword(passwordEncoder.encode(driverDTO.getPassword()));
        return driverRepository.save(driver).getId();
    }

    @Override
    public void update(final Long id, final DriverDTO driverDTO) {
        final Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new NotFoundException());
        mapToEntity(driverDTO, driver);
        driverRepository.save(driver);
    }

    @Override
    public void delete(final Long id) {
        driverRepository.deleteById(id);
    }

    private DriverDTO mapToDTO(final Driver driver, final DriverDTO driverDTO) {
        driverDTO.setId(driver.getId());
        driverDTO.setFirstName(driver.getFirstName());
        driverDTO.setLastName(driver.getLastName());
        driverDTO.setEmail(driver.getEmail());
        driverDTO.setPhoneNumber(driver.getPhoneNumber());
        return driverDTO;
    }

    private Driver mapToEntity(final DriverDTO driverDTO, final Driver driver) {
        driver.setFirstName(driverDTO.getFirstName());
        driver.setLastName(driverDTO.getLastName());
        driver.setEmail(driverDTO.getEmail());
        driver.setPhoneNumber(driverDTO.getPhoneNumber());
        return driver;
    }

    @Transactional
    @Override
    public String getReferencedWarning(final Long id) {
        final Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new NotFoundException());
        if (!driver.getMissions().isEmpty()) {
            return WebUtils.getMessage("driver.carRental.manyToOne.referenced", driver.getMissions().iterator().next().getId());
        }
        return null;
    }

}
