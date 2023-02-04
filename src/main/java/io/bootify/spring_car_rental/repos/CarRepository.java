package io.bootify.spring_car_rental.repos;

import io.bootify.spring_car_rental.domain.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface CarRepository extends JpaRepository<Car, Long> {
    Optional<Car> findFirstByCarDescription_IdAndAndIsAvailableIsTrue(Long carDescriptionId);
}
