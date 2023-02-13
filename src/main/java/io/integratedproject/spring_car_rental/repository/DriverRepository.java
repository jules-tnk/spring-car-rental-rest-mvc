package io.integratedproject.spring_car_rental.repository;

import io.integratedproject.spring_car_rental.entity.user_management.Driver;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {

    List<Driver> findAllById(Long id, Sort sort);
    Optional<Driver> findFirstByIsAvailableIsTrue();

}
