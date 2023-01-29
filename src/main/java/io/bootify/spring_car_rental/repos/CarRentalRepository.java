package io.bootify.spring_car_rental.repos;

import io.bootify.spring_car_rental.domain.CarRental;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CarRentalRepository extends JpaRepository<CarRental, Long> {
}
