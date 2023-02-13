package io.integratedproject.spring_car_rental.service.impl;

import io.integratedproject.spring_car_rental.entity.Car;
import io.integratedproject.spring_car_rental.entity.CarDescription;
import io.integratedproject.spring_car_rental.DTO.CarDTO;
import io.integratedproject.spring_car_rental.entity.CarRental;
import io.integratedproject.spring_car_rental.repository.CarDescriptionRepository;
import io.integratedproject.spring_car_rental.repository.CarRentalRepository;
import io.integratedproject.spring_car_rental.repository.CarRepository;
import io.integratedproject.spring_car_rental.service.interf.CarService;
import io.integratedproject.spring_car_rental.util.NotFoundException;
import io.integratedproject.spring_car_rental.util.WebUtils;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final CarDescriptionRepository carDescriptionRepository;
    private final CarRentalRepository carRentalRepository;

    public CarServiceImpl(final CarRepository carRepository,
            final CarDescriptionRepository carDescriptionRepository,
                          CarRentalRepository carRentalRepository) {
        this.carRepository = carRepository;
        this.carDescriptionRepository = carDescriptionRepository;
        this.carRentalRepository = carRentalRepository;
    }

    @Override
    public List<CarDTO> findAll() {
        final List<Car> cars = carRepository.findAll(Sort.by("id"));
        return cars.stream()
                .map((car) -> mapToDTO(car, new CarDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public CarDTO get(final Long id) {
        return carRepository.findById(id)
                .map(car -> mapToDTO(car, new CarDTO()))
                .orElseThrow(() -> new NotFoundException());
    }

    @Override
    public Long create(final CarDTO carDTO) {
        final Car car = new Car();
        mapToEntity(carDTO, car);
        return carRepository.save(car).getId();
    }

    @Override
    public void update(final Long id, final CarDTO carDTO) {
        final Car car = carRepository.findById(id)
                .orElseThrow(() -> new NotFoundException());
        mapToEntity(carDTO, car);
        carRepository.save(car);
    }

    public Car selectAvailableCar(CarDescription carDescription, LocalDate startDate, LocalDate endDate){
        List<Car> allCars = carRepository.findByCarDescription(carDescription);
        for (Car car : allCars) {
            if (isAvailable(car, startDate, endDate)){
                return car;
            }
        }
        return null;
    }

    public boolean isAvailable(Car car, LocalDate startDate, LocalDate endDate){
        List<CarRental> carRentals = carRentalRepository.findByCar(car);
        for (CarRental carRental : carRentals) {
            boolean conditions1 = startDate.isAfter(carRental.getStartDate()) && startDate.isBefore(carRental.getEndDate());
            boolean conditions2 = endDate.isAfter(carRental.getStartDate()) && endDate.isBefore(carRental.getEndDate());
            if (conditions1 || conditions2){
                return false;
            }
        }
        return true;
    }

    @Override
    public void delete(final Long id) {
        carRepository.deleteById(id);
    }

    private CarDTO mapToDTO(final Car car, final CarDTO carDTO) {
        carDTO.setId(car.getId());
        carDTO.setLicensePlate(car.getLicensePlate());
        carDTO.setIsAvailable(car.getIsAvailable());
        carDTO.setCarDescription(car.getCarDescription() == null ? null : car.getCarDescription().getId());
        return carDTO;
    }

    private Car mapToEntity(final CarDTO carDTO, final Car car) {
        car.setLicensePlate(carDTO.getLicensePlate());
        car.setIsAvailable(carDTO.getIsAvailable());
        final CarDescription carDescription = carDTO.getCarDescription() == null ? null : carDescriptionRepository.findById(carDTO.getCarDescription())
                .orElseThrow(() -> new NotFoundException("carDescription not found"));
        car.setCarDescription(carDescription);
        return car;
    }

    @Transactional
    @Override
    public String getReferencedWarning(final Long id) {
        final Car car = carRepository.findById(id)
                .orElseThrow(() -> new NotFoundException());
        if (!car.getCarCarRentals().isEmpty()) {
            return WebUtils.getMessage("car.carRental.oneToMany.referenced", car.getCarCarRentals().iterator().next().getId());
        }
        return null;
    }

}
