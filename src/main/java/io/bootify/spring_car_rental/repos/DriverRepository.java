package io.bootify.spring_car_rental.repos;

import io.bootify.spring_car_rental.domain.user_management.Driver;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DriverRepository extends JpaRepository<Driver, Long> {

    List<Driver> findAllById(Long id, Sort sort);
    Optional<Driver> findFirstByIsAvailableIsTrue();

}
