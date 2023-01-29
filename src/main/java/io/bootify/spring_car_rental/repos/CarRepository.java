package io.bootify.spring_car_rental.repos;

import io.bootify.spring_car_rental.domain.Car;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CarRepository extends JpaRepository<Car, Long> {
    Car findFirstByCarDescription_IdAndAndIsAvailableIsTrue(Long carDescriptionId);
}
