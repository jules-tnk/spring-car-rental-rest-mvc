package io.integratedproject.spring_car_rental.repository;

import io.integratedproject.spring_car_rental.entity.Car;
import io.integratedproject.spring_car_rental.entity.CarDescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    Optional<Car> findFirstByCarDescription_IdAndAndIsAvailableIsTrue(Long carDescriptionId);

    List<Car> findByCarDescription(CarDescription carDescription);

}
