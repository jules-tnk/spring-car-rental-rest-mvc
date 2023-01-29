package io.bootify.spring_car_rental.service.impl;

import io.bootify.spring_car_rental.domain.CarDescription;
import io.bootify.spring_car_rental.DTO.CarDescriptionDTO;
import io.bootify.spring_car_rental.repos.CarDescriptionRepository;
import io.bootify.spring_car_rental.service.interf.CarDescriptionService;
import io.bootify.spring_car_rental.util.NotFoundException;
import io.bootify.spring_car_rental.util.WebUtils;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class CarDescriptionServiceImpl implements CarDescriptionService {

    private final CarDescriptionRepository carDescriptionRepository;

    public CarDescriptionServiceImpl(final CarDescriptionRepository carDescriptionRepository) {
        this.carDescriptionRepository = carDescriptionRepository;
    }

    @Override
    public List<CarDescriptionDTO> findAll(final String filter) {
        List<CarDescription> carDescriptions;
        final Sort sort = Sort.by("id");
        if (filter != null) {
            Long longFilter = null;
            try {
                longFilter = Long.parseLong(filter);
            } catch (final NumberFormatException numberFormatException) {
                // keep null - no parseable input
            }
            carDescriptions = carDescriptionRepository.findAllById(longFilter, sort);
        } else {
            carDescriptions = carDescriptionRepository.findAll(sort);
        }
        return carDescriptions.stream()
                .map((carDescription) -> mapToDTO(carDescription, new CarDescriptionDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public CarDescriptionDTO get(final Long id) {
        return carDescriptionRepository.findById(id)
                .map(carDescription -> mapToDTO(carDescription, new CarDescriptionDTO()))
                .orElseThrow(() -> new NotFoundException());
    }

    @Override
    public Long create(final CarDescriptionDTO carDescriptionDTO) {
        final CarDescription carDescription = new CarDescription();
        mapToEntity(carDescriptionDTO, carDescription);
        return carDescriptionRepository.save(carDescription).getId();
    }

    @Override
    public void update(final Long id, final CarDescriptionDTO carDescriptionDTO) {
        final CarDescription carDescription = carDescriptionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException());
        mapToEntity(carDescriptionDTO, carDescription);
        carDescriptionRepository.save(carDescription);
    }

    @Override
    public void delete(final Long id) {
        carDescriptionRepository.deleteById(id);
    }

    private CarDescriptionDTO mapToDTO(final CarDescription carDescription,
            final CarDescriptionDTO carDescriptionDTO) {
        carDescriptionDTO.setId(carDescription.getId());
        carDescriptionDTO.setModel(carDescription.getModel());
        carDescriptionDTO.setBrand(carDescription.getBrand());
        carDescriptionDTO.setColor(carDescription.getColor());
        carDescriptionDTO.setImgUrl(carDescription.getImgUrl());
        carDescriptionDTO.setPricePerHour(carDescription.getPricePerHour());
        carDescriptionDTO.setMaxSpeed(carDescription.getMaxSpeed());
        carDescriptionDTO.setMaxPower(carDescription.getMaxPower());
        carDescriptionDTO.setSeatNumber(carDescription.getSeatNumber());
        return carDescriptionDTO;
    }

    private CarDescription mapToEntity(final CarDescriptionDTO carDescriptionDTO,
            final CarDescription carDescription) {
        carDescription.setModel(carDescriptionDTO.getModel());
        carDescription.setBrand(carDescriptionDTO.getBrand());
        carDescription.setColor(carDescriptionDTO.getColor());
        carDescription.setImgUrl(carDescriptionDTO.getImgUrl());
        carDescription.setPricePerHour(carDescriptionDTO.getPricePerHour());
        carDescription.setMaxSpeed(carDescriptionDTO.getMaxSpeed());
        carDescription.setMaxPower(carDescriptionDTO.getMaxPower());
        carDescription.setSeatNumber(carDescriptionDTO.getSeatNumber());
        return carDescription;
    }

    @Transactional
    @Override
    public String getReferencedWarning(final Long id) {
        final CarDescription carDescription = carDescriptionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException());
        if (!carDescription.getCarDescriptionCars().isEmpty()) {
            return WebUtils.getMessage("carDescription.car.oneToMany.referenced", carDescription.getCarDescriptionCars().iterator().next().getId());
        }
        return null;
    }

}
