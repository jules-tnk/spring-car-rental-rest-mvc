package io.bootify.spring_car_rental.repos;

import io.bootify.spring_car_rental.domain.CarRental;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CarRentalRepository extends JpaRepository<CarRental, Long> {
    List<CarRental> findByTenant_EmailOrderById(String email);
    List<CarRental> findByDriver_EmailOrderById(String email);
}
