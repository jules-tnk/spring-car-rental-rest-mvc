package io.integratedproject.spring_car_rental.repository;

import io.integratedproject.spring_car_rental.entity.Car;
import io.integratedproject.spring_car_rental.entity.CarRental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRentalRepository extends JpaRepository<CarRental, Long> {
    List<CarRental> findByTenant_EmailOrderById(String email);
    List<CarRental> findByDriver_EmailOrderById(String email);

    List<CarRental> findByIsWithDriverIsTrue();
    List<CarRental> findByCar(Car car);
}
