package io.bootify.spring_car_rental.service.impl;

import io.bootify.spring_car_rental.domain.user_management.Agent;
import io.bootify.spring_car_rental.domain.user_management.AppUser;
import io.bootify.spring_car_rental.domain.Car;
import io.bootify.spring_car_rental.domain.CarRental;
import io.bootify.spring_car_rental.domain.user_management.Driver;
import io.bootify.spring_car_rental.DTO.CarRentalDTO;
import io.bootify.spring_car_rental.repos.AgentRepository;
import io.bootify.spring_car_rental.repos.AppUserRepository;
import io.bootify.spring_car_rental.repos.CarRentalRepository;
import io.bootify.spring_car_rental.repos.CarRepository;
import io.bootify.spring_car_rental.repos.DriverRepository;
import io.bootify.spring_car_rental.service.interf.CarRentalService;
import io.bootify.spring_car_rental.util.NotFoundException;
import io.bootify.spring_car_rental.util.WebUtils;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class CarRentalServiceImpl implements CarRentalService {

    private final CarRentalRepository carRentalRepository;
    private final CarRepository carRepository;
    private final AppUserRepository appUserRepository;
    private final AgentRepository agentRepository;
    private final DriverRepository driverRepository;

    public CarRentalServiceImpl(final CarRentalRepository carRentalRepository,
            final CarRepository carRepository, final AppUserRepository appUserRepository,
            final AgentRepository agentRepository, final DriverRepository driverRepository) {
        this.carRentalRepository = carRentalRepository;
        this.carRepository = carRepository;
        this.appUserRepository = appUserRepository;
        this.agentRepository = agentRepository;
        this.driverRepository = driverRepository;
    }

    @Override
    public List<CarRentalDTO> findAll() {
        final List<CarRental> carRentals = carRentalRepository.findAll(Sort.by("id"));
        return carRentals.stream()
                .map((carRental) -> mapToDTO(carRental, new CarRentalDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public CarRentalDTO get(final Long id) {
        return carRentalRepository.findById(id)
                .map(carRental -> mapToDTO(carRental, new CarRentalDTO()))
                .orElseThrow(() -> new NotFoundException());
    }

    @Override
    public Long create(final CarRentalDTO carRentalDTO) {
        final CarRental carRental = new CarRental();
        mapToEntity(carRentalDTO, carRental);
        return carRentalRepository.save(carRental).getId();
    }

    @Override
    public void update(final Long id, final CarRentalDTO carRentalDTO) {
        final CarRental carRental = carRentalRepository.findById(id)
                .orElseThrow(() -> new NotFoundException());
        mapToEntity(carRentalDTO, carRental);
        carRentalRepository.save(carRental);
    }

    @Override
    public void delete(final Long id) {
        carRentalRepository.deleteById(id);
    }

    private CarRentalDTO mapToDTO(final CarRental carRental, final CarRentalDTO carRentalDTO) {
        carRentalDTO.setId(carRental.getId());
        carRentalDTO.setStartDate(carRental.getStartDate());
        carRentalDTO.setEndDate(carRental.getEndDate());
        carRentalDTO.setIsPaymentCompleted(carRental.getIsPaymentCompleted());
        carRentalDTO.setStatus(carRental.getStatus());
        carRentalDTO.setIsWithDriver(carRental.getIsWithDriver());
        carRentalDTO.setCar(carRental.getCar() == null ? null : carRental.getCar().getId());
        carRentalDTO.setTenant(carRental.getTenant() == null ? null : carRental.getTenant().getId());
        carRentalDTO.setAgentForConfirmation(carRental.getAgentForConfirmation() == null ? null : carRental.getAgentForConfirmation().getId());
        carRentalDTO.setAgentForReturn(carRental.getAgentForReturn() == null ? null : carRental.getAgentForReturn().getId());
        carRentalDTO.setDriver(carRental.getDriver() == null ? null : carRental.getDriver().getId());
        return carRentalDTO;
    }

    private CarRental mapToEntity(final CarRentalDTO carRentalDTO, final CarRental carRental) {
        carRental.setStartDate(carRentalDTO.getStartDate());
        carRental.setEndDate(carRentalDTO.getEndDate());
        carRental.setIsPaymentCompleted(carRentalDTO.getIsPaymentCompleted());
        carRental.setStatus(carRentalDTO.getStatus());
        carRental.setIsWithDriver(carRentalDTO.getIsWithDriver());
        final Car car = carRentalDTO.getCar() == null ? null : carRepository.findById(carRentalDTO.getCar())
                .orElseThrow(() -> new NotFoundException("car not found"));
        carRental.setCar(car);
        final AppUser tenant = carRentalDTO.getTenant() == null ? null : appUserRepository.findById(carRentalDTO.getTenant())
                .orElseThrow(() -> new NotFoundException("tenant not found"));
        carRental.setTenant(tenant);
        final Agent agentForConfirmation = carRentalDTO.getAgentForConfirmation() == null ? null : agentRepository.findById(carRentalDTO.getAgentForConfirmation())
                .orElseThrow(() -> new NotFoundException("agentForConfirmation not found"));
        carRental.setAgentForConfirmation(agentForConfirmation);
        final Agent agentForReturn = carRentalDTO.getAgentForReturn() == null ? null : agentRepository.findById(carRentalDTO.getAgentForReturn())
                .orElseThrow(() -> new NotFoundException("agentForReturn not found"));
        carRental.setAgentForReturn(agentForReturn);
        final Driver driver = carRentalDTO.getDriver() == null ? null : driverRepository.findById(carRentalDTO.getDriver())
                .orElseThrow(() -> new NotFoundException("driver not found"));
        carRental.setDriver(driver);
        return carRental;
    }

    @Transactional
    @Override
    public String getReferencedWarning(final Long id) {
        final CarRental carRental = carRentalRepository.findById(id)
                .orElseThrow(() -> new NotFoundException());
        if (!carRental.getPaymentPayments().isEmpty()) {
            return WebUtils.getMessage("carRental.payment.oneToMany.referenced", carRental.getPaymentPayments().iterator().next().getId());
        }
        return null;
    }

}
